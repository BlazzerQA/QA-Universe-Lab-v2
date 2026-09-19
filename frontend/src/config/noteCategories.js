/** Category folder name → public icon path. Add a file under /icons and a key here. */
export const CATEGORY_LOGOS = {
  api: '/icons/api.png',
  git: '/icons/git.png',
  interview: '/icons/interview.png',
  java: '/icons/java.svg',
  junit: '/icons/junit.png',
  manual: '/icons/manual.png',
  python: '/icons/python.webp',
  selenium: '/icons/selenium.png',
  sql: '/icons/sql.png'
}

export function categoryLogo(category) {
  if (!category) return ''
  return CATEGORY_LOGOS[category.toLowerCase()] || ''
}
