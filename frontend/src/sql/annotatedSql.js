/** Aligns trailing `--` notes, then puts the spoken summary one blank line below the query. */
export function annotatedSql(lines, readsAs) {
  const width = Math.max(0, ...lines.map((line) => line.code.length))
  const body = lines
    .map(({ code, note }) => (note ? `${code.padEnd(width)}  -- ${note}` : code))
    .join('\n')
  return `${body}\n\n-- читается как: ${readsAs}`
}
