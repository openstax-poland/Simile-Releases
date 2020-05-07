import { Helpers } from './interfaces'
/**
 * Take url to the first page of the book from openstax.org / cnx.org / herokuapp.com
 * or any subdomain like staging.openstax.org / staging.cnx.org / rex-web-issue-947-np39vpknfjij.herokuapp.com
 * and return object with helpfull data and functions.
 *
 * @param url url to the first page in the book on openstax.org or any subdomain.
 */
const parseUrl = (url: string): Helpers => {
  if (!/https:\/\//.test(url)) {
    throw new Error('Provided url has to start with "https://".')
  }

  return {
    domain: getDomainUrl(url),
    baseUrlForBook: getBaseUrlForBook(url),
    nextPageSelector: getNextPageSelector(url),
    mainContentSelector: getMainContentSelector(url),
    tocSelector: getTocSelector(url),
    bookTitleSelector: getBookTitleSelector(url),
    isOpenstaxOrg: isOpenstaxOrg(url),
    isHerokuappCom: isHerokuappCom(url),
    isCnxOrg: isCnxOrg(url),
    isRex: isOpenstaxOrg(url) || isHerokuappCom(url),
    createNextPageUrl,
    createFileNameFromUrl,
  }
}

export default parseUrl

const isOpenstaxOrg = (url: string): boolean => Boolean(url.match(/https\:\/\/.*openstax\.org/g))
const isHerokuappCom = (url: string): boolean => Boolean(url.match(/https\:\/\/.*herokuapp\.com/g))
const isCnxOrg = (url: string): boolean => Boolean(url.match(/https\:\/\/.*cnx\.org/g))

/**
 * Get everything between https and domain.xxx
 * Throws an error if provided url is not supported.
 *
 * @param {string} url
 */
const getDomainUrl = (url: string): string => {
  if (isOpenstaxOrg(url)) return url.match(/https\:\/\/.*openstax\.org/g)![0]
  if (isHerokuappCom(url)) return url.match(/https\:\/\/.*herokuapp\.com/g)![0]
  if (isCnxOrg(url)) return url.match(/https\:\/\/.*cnx\.org/g)![0]
  throw new Error('Provided url must be in openstax.org, cnx.org or herokuapp.com domain.')
}

/**
 * Get base url for pages in specific book. It will be merged with next page url
 * to create url for the next page.
 * Throws an error if provided url is not supported.
 *
 * @param {string} url
 */
const getBaseUrlForBook = (url: string): string => {
  if (isOpenstaxOrg(url) || isHerokuappCom(url)) {
    const domain = getDomainUrl(url)
    const temp = url.match(/\/books\/.*\/pages\//)
    if (!temp) throw new Error(`Couldn't obtain book name from provided url`)
    const bookName = temp[0].replace(/(\/books\/|\/pages\/)/g, '')
    return cleanUrl(`${domain}/books/${bookName}/pages/`)
  } else if (isCnxOrg(url)) {
    // In cnx.org next page url contains everything which is required so we can pass domain name
    return url.match(/https\:\/\/.*cnx\.org/g)![0]
  }
  throw new Error('Provided url must be in openstax.org, cnx.org or herokuapp.com domain.')
}

/**
 * Make sure that url does not contain multiple slashes.
 *
 * @param {string} url
 */
export const cleanUrl = (url: string) => url.replace(/\/+/g, '/').replace(/https:\//g, 'https://')

/**
 * CSS selector for next page button depends on url domain.
 *
 * @param {string} url
 */
const getNextPageSelector = (url: string) => isOpenstaxOrg(url) || isHerokuappCom(url)
  ? '[aria-label="Next Page"]'
  : 'a.nav.next'

/**
 * CSS selector for div which holds book content depends on url domain.
 *
 * @param {string} url
 */
const getMainContentSelector = (url: string) => isOpenstaxOrg(url) || isHerokuappCom(url)
  ? '#main-content > div'
  : '.media-body #content > div'

/**
 * CSS selector for div which holds ToC depends on url domain.
 *
 * @param {string} url
 */
const getTocSelector = (url: string) => isOpenstaxOrg(url) || isHerokuappCom(url)
  ? '[data-testid="toc"]'
  : '.toc'

/**
 * CSS selector for book title depends on url domain.
 *
 * @param {string} url
 */
const getBookTitleSelector = (url: string) => isOpenstaxOrg(url) || isHerokuappCom(url)
  ? '[data-testid="bookbanner"] a'
  : '.title h1'

/**
 * Make valid url for next page from parts.
 *
 * @param {string} baseUrl
 * @param {string} nextPageUrl
 */
const createNextPageUrl = (baseUrl: string, nextPageUrl: string) => {
  // nextPageUrl from openstax.org and herokuapp.com may contain multiple "../"
  const parsed = nextPageUrl.replace(/\.\.\//g, '')
    // nextPageUrl from openstax.org and herokuapp.com may start with /bookName/pages/
    .replace(/.*\/pages\//, '')
  return cleanUrl(`${baseUrl}/${parsed}`)
}

/**
 * Remove search query from string
 * Remove slash from the end of url
 * Replace slashes with "+" and colons with "_".
 * Add prefix and suffix and decodeURIComponent.
 * Set file format.
 *
 * @param {string} url
 * @param {string} prefix
 * @param {string} suffix
 * @param {png|jpg} format (optional), default png
 */
const createFileNameFromUrl = (url: string, prefix: string, suffix: string, format: 'png' | 'jpg' = 'png') => {
  const parsedUrl = url.replace(/\?.*/, '').replace(/\/$/, '').replace(/\//g, '+').replace(/\:/g, '_')
  return prefix + decodeURIComponent(parsedUrl) + suffix + `.${format}`
}
