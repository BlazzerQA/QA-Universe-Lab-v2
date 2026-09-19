import editorWorker from 'monaco-editor/editor/editor.worker?worker'
import 'monaco-editor/languages/definitions/sql/register.js'

globalThis.MonacoEnvironment = {
  getWorker() {
    return new editorWorker()
  }
}
