interface Cookie {
  name: string
  value: string
  url?: string
  domain?: string
  path?: string
  expires?: number
  httpOnly?: boolean
  secure?: boolean
  sameSite?: 'Strict' | 'Lax'
}

const defaultCookies: Cookie[] = [
  { name: 'pi_pageview_count', value: '1' },
  { name: 'pi_visit_track', value: 'true' },
  { name: 'pi_visit_count', value: '1' },
  { name: 'pulse_insights_udid', value: 'b759213f-aee7-467b-9a69-36b8a633488b' },
]

export default defaultCookies
