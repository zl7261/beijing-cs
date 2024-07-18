<script setup>
import FileViewer from "./FileViewer.vue";
import {reactive, ref, watch} from "vue";

const props = defineProps({modalType: String || Boolean, record: Object, onEdit: Function})
const emits = defineEmits(["update:open", 'edit'])

const formModel = reactive({
  name: '',
  remark: '',
})

watch(() => props, (newProps) => {
  if (newProps && newProps.modalType === 'edit') {
    console.log(newProps.record);
    formModel.name = newProps.record.name
    formModel.remark = newProps.record.remark
  } else {
    formModel.name = ''
    formModel.remark = ''
  }
}, {deep: true})

function handleOk() {
  emits('edit', formModel)
  handleClose()
}

function handleClose() {
  fileViewer.value?.clearFile()
  emits('update:modalType', false)
}
const fileViewer = ref(null)
</script>

<template>
  <a-modal :open="Boolean(props.modalType)" title="文件预览" @ok="handleOk" @cancel="handleClose"
           style="top: 20px"
           width="90vw">
    <template v-if="modalType==='view'">
      <a-form disabled>
        <a-form-item label="名称">
          <a-input :value="record.name"/>
        </a-form-item>
        <a-form-item label="大小">
          <a-input :value="record.fileSize"/>
        </a-form-item>
        <a-form-item label="拓展名">
          <a-input :value="record.extensions"/>
        </a-form-item>
        <a-form-item label="文件渲染">
          <FileViewer :file="record.raw"
                      ref="fileViewer"
                      class="max-h-96 overflow-auto"/>
        </a-form-item>
      </a-form>
    </template>
    <template v-if="modalType==='edit'">
      <a-form v-model="formModel">
        <a-form-item label="名称">
          <a-input v-model:value="formModel.name" disabled/>
        </a-form-item>
        <a-form-item label="备注">
          <a-input v-model:value="formModel.remark"/>
        </a-form-item>
      </a-form>
    </template>
  </a-modal>
</template>

<style scoped>

</style>