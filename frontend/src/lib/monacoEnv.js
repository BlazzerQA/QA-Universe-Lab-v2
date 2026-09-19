import editorWorker from 'monaco-editor/editor/editor.worker?worker'
import jsonWorker from 'monaco-editor/languages/features/json/json.worker?worker'
import 'monaco-editor/languages/definitions/sql/register.js'
import 'monaco-editor/languages/features/json/register.js'

globalThis.MonacoEnvironment = {
  getWorker(_workerId, label) {
    if (label === 'json') {
      return new jsonWorker()
    }
    return new editorWorker()
  }
}
