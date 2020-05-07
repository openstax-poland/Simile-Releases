import * as yargs from 'yargs'
import puppeteer from 'puppeteer'
import path from 'path'
import fs from 'fs'

import defaultCookies from './config/cookies'
import parseUrl, { cleanUrl } from './helpers/parseUrl'
import { Chapter, Module, Toc } from './helpers/interfaces'

const argv = yargs
  .option('url', {
    alias: 'u',
    describe: 'Url from which script should start scanning book - allowed domains: openxtax.org and cnx.org',
    type: 'string',
  })
  .option('only-toc', {
    describe: 'Scan only Table of Contents from specific url.',
  })
  .options('width', {
    alias: 'w',
    describe: 'Width of web browser which will be used to display page. Default is 1920px.',
    type: 'number',
  })
  .option('height', {
    alias: 'h',
    describe: 'Height of web browser which will be used to display page. Default is 1080px.',
    type: 'number',
  })
  .options('path', {
    alias: 'p',
    describe: 'Path where screen should be saved.',
    type: 'string',
  })
  .options('prefix', {
    describe: 'String which will be added at the beggining of name for each screenshot.',
    type: 'string',
  })
  .options('suffix', {
    describe: 'String which will be added at the end of name for each screenshot.',
    type: 'string',
  })
  .options('single-page', {
    describe: 'If passed, scanner will exit after scanning first page.'
  })
  .demandOption(['url'])
  .help()
  .argv

const startUrl = argv.url
const width = argv.width || 1920
const height = argv.height || 1080
const outputPath = path.resolve(argv.path || '../ScanDB/')
const prefix = argv.prefix || ''
const suffix = argv.suffix || ''

const {
  baseUrlForBook,
  domain,
  nextPageSelector,
  mainContentSelector,
  tocSelector,
  isRex,
  isCnxOrg,
  createFileNameFromUrl,
  createNextPageUrl,
} = parseUrl(startUrl)

const start = async () => {
  console.log('Lauching puppeteer...')
  const browser = await puppeteer.launch()

  console.log('Opening new tab...')
  const page = await browser.newPage()

  console.log(`Setting viewport to ${width}x${height}px`)
  await page.setViewport({
    width: 1920,
    height: 1080,
    deviceScaleFactor: 1,
  })

  console.log('Setting default cookies...')
  await page.setCookie(...defaultCookies.map(c => ({ domain: domain.replace('https://', ''), ...c })))

  if (argv["only-toc"]) {
    console.log('Opening', startUrl)
    await page.goto(startUrl, { waitUntil: 'networkidle0', timeout: 0 })

    const chapters = await page.evaluate((tocSelector, isRex, baseUrlForBook) => {
      const getSubchapters = (el: Element, isRex: boolean): Toc | undefined => {
        const subchapters: Toc = []

        const list = el.querySelector(isRex ? 'ol' : 'ul')
        if (!list) return undefined
        list.setAttribute('id', 'temp_id')
        // Get only direct children
        const items = el.querySelectorAll(`#temp_id > li`)
        list.removeAttribute('id')
        if (!items.length) return undefined

        for (const item of Array.from(items)) {
          const title = isRex
            ? (item.querySelector('a, summary')!.textContent || 'Unknown title').trim()
            : (item.querySelector('.name-wrapper')!.textContent || 'Unknown title').trim()
          const anchorOrWrapper = item.querySelector('a')
          const href = anchorOrWrapper ? anchorOrWrapper.getAttribute('href') : undefined
          const url = href ? `${baseUrlForBook}${href}` : undefined
          const chapters = getSubchapters(item as Element, isRex)

          if (chapters) {
            subchapters.push({
              type: 'chapter',
              title,
              chapters,
            } as Chapter)
          } else if (url) {
            subchapters.push({
              type: 'module',
              title,
              url,
            } as Module)
          }
        }

        return subchapters
      }

      const toc = document.querySelector(tocSelector) as HTMLElement
      const chapters: Toc = getSubchapters(toc, isRex) as Toc

      return chapters
    }, tocSelector, isRex, baseUrlForBook)

    console.log('Saving Table of Contents...')
    fs.writeFile(
      `${outputPath.replace(/\\/g, '/')}/${prefix}toc${suffix}.json`,
      JSON.stringify(chapters, null, 2),
      err => { if (err) throw err }
    );

    console.log('Closing browser...')
    await browser.close()

    return
  }

  const makeScreenshotOnPage = async (url: string) => {
    console.log('Opening', url)
    await page.goto(url, { waitUntil: 'networkidle0', timeout: 0 })

    console.log('Obtaining pageUrl, nextPageUrl, hiding top bars, moving content above popups...')
    const { pageUrl, nextPageUrl } = await page.evaluate((nextPageSelector, mainContentSelector, isRex, isCnxOrg) => {
      // Selector for elements to hide for openstax.org
      let selectorsForElementsToHide = '[class^=BookBanner__BarWrapper], [class^=styled__BarWrapper], [data-analytics-region="signup CTA"]'

      // Selector for elements to hide for cnx.org
      if (isCnxOrg) {
        selectorsForElementsToHide = '.pinnable, .media-header'

        // cnx.org is adding margin-top to then #main-content when scrolled down.
        // We want to disable it since it's affecting screenshots.
        window.addEventListener('scroll', function (event) {
          event.stopPropagation()
        }, true)
      }

      // Hide elements
      Array.from(document.querySelectorAll(selectorsForElementsToHide))
        .forEach(el => (el as HTMLElement).style.display = 'none')

      // Open all Show Solution sections
      const buttons = document.querySelectorAll('[data-type="solution"] button') as NodeListOf<HTMLButtonElement>
      buttons.forEach(btn => btn.click())

      if (isRex) {
        // Moving content above all other elements so popup are not visible
        const content = document.querySelector(mainContentSelector) as HTMLElement
        if (content) {
          content.style.position = 'relative'
          content.style.zIndex = '9999999999'
          content.style.background = '#ffffff'
        }
      }

      const nextPageButton = document.querySelector(nextPageSelector) as HTMLElement | null
      let nextPageUrl = ''
      if (nextPageButton && nextPageButton.getAttribute('href')) {
        nextPageUrl = nextPageButton.getAttribute('href')!
      }

      return {
        pageUrl: window.location.href,
        nextPageUrl,
      }
    }, nextPageSelector, mainContentSelector, isRex, isCnxOrg)

    console.log('Obtaining content element...')
    const content = await page.waitForSelector(mainContentSelector)
    if (!content) {
      console.error(`Error: Couldn't find main content selector: "${mainContentSelector}" in the page.`)
      return
    }

    console.log('Taking screenshot...')
    await content.screenshot({
      type: 'png',
      path: cleanUrl(`${outputPath.replace(/\\/g, '/')}/${createFileNameFromUrl(pageUrl, prefix, suffix, 'png')}`)
    })

    if (argv['single-page']) return

    if (nextPageUrl) {
      console.log('Moving to the next page...')
      await makeScreenshotOnPage(createNextPageUrl(baseUrlForBook, nextPageUrl))
    } else {
      console.log('Next page was not found.')
    }
  }

  await makeScreenshotOnPage(startUrl)

  console.log('Closing browser...')
  await browser.close()
}

start().catch((err) => {
  console.error("Error:", err);
  process.exit(1)
})
