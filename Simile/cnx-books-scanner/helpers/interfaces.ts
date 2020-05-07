export interface Helpers {
  domain: string
  baseUrlForBook: string
  nextPageSelector: string
  mainContentSelector: string
  tocSelector: string
  bookTitleSelector: string
  isOpenstaxOrg: boolean
  isHerokuappCom: boolean
  isCnxOrg: boolean
  isRex: boolean
  createNextPageUrl: (baseUrl: string, nextPageUrl: string) => string
  createFileNameFromUrl: (url: string, prefix: string, suffix: string, format?: 'png' | 'jpg') => string
}

export interface Module {
  type: 'module'
  title: string
  url: string
}

export interface Chapter {
  type: 'chapter'
  title: string
  chapters: Module | Chapter[]
}

export type Toc = (Chapter | Module)[]
