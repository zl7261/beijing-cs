<template>
  <div>
    <div v-if="isText">
      <pre>{{ fileContent }}</pre>
    </div>
    <div v-else-if="isImage">
      <a-image
          :src="imageUrl"
          :alt="file.name"
      />
    </div>
    <div v-else-if="isPDF">
      <iframe :src="pdfUrl" :title="file.name" width="100%" height="600"></iframe>
    </div>
    <div v-else-if="isXlsx">
      <a-table :data-source="firstSheetData"
               :columns="firstSheetColumns"
               :scroll="{ y: 200, x: 1000 }"
               :pagination="false"
               :loading="parsing"
      />
    </div>
    <div v-else>
      <p>暂时不支持预览该类型的文件: {{ file.type }}</p>
    </div>
  </div>
</template>

<script setup>
import {computed, nextTick, ref, watch} from 'vue'
import * as Excel from 'exceljs'
import {message} from "ant-design-vue";

const props = defineProps({file: Object})

const fileContent = ref('')
const imageUrl = ref('')
const pdfUrl = ref('')
const firstSheetData = ref([])
const firstSheetColumns = ref([])

const isText = computed(() => props.file.type.startsWith('text/'))
const isImage = computed(() => props.file.type.startsWith('image/'))
const isPDF = computed(() => props.file.type === 'application/pdf')
const isXlsx = computed(() => ['application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'].includes(props.file.type))

function loadFileContent() {
  if (isText.value) {
    loadTextContent()
  } else if (isImage.value) {
    loadImageContent()
  } else if (isPDF.value) {
    loadPDFContent()
  } else if (isXlsx.value) {
    loadExcelContent()
  }
}

function clearFile() {
  if (isText.value) {
    URL.revokeObjectURL(isText.value)
  } else if (isImage.value) {
    URL.revokeObjectURL(isImage.value)
  } else if (isPDF.value) {
    URL.revokeObjectURL(isPDF.value)
  } else if (isXlsx.value) {
    URL.revokeObjectURL(isXlsx.value)
  }
}

defineExpose({clearFile})

function loadTextContent() {
  const reader = new FileReader()
  reader.onload = () => {
    fileContent.value = reader.result
  }
  reader.readAsText(props.file)
}

function loadImageContent() {
  imageUrl.value = URL.createObjectURL(props.file)

}

function loadPDFContent() {
  pdfUrl.value = URL.createObjectURL(props.file)
}

const parsing = ref(false)

async function loadExcelContent() {
  parsing.value = true
  const wb = new Excel.Workbook();
  const reader = new FileReader()

  reader.readAsArrayBuffer(props.file)
  reader.onload = async () => {
    try {

      const buffer = reader.result;
      message.success('xlsx文件上传成功,请打开控制台查看数据')
      const workbook = await wb.xlsx.load(buffer)
      workbook.eachSheet(async (sheet) => {
        const sheetValues = sheet.getSheetValues()
        const columns = sheetValues[1]
        firstSheetColumns.value = columns.map(item => {
          return {
            title: item,
            dataIndex: item,
          }
        })
        firstSheetData.value = sheetValues.slice(0).map((row) => {
          return row.reduce((obj, value, index) => {
            obj[columns[index]] = value
            return obj
          }, {})
        })

        await nextTick()
        parsing.value = false
      })
    } finally {
      parsing.value = false
    }
  }
}

watch(() => props.file, () => {
  loadFileContent()
}, {deep: true, immediate: true})


</script>