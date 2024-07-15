<script setup>

import {h, ref} from "vue";
import {PlusOutlined, SearchOutlined, UploadOutlined} from "@ant-design/icons-vue"
import {message} from "ant-design-vue";
import dayjs from "dayjs";
import ViewModal from "./ViewModal.vue";

const dataSource = ref([],)
const columns = ref([
      {
        title: '文件名称',
        dataIndex: 'name',
        width: 250,
      },
      {
        title: '大小',
        dataIndex: 'fileSize',
        width: 150,
      },
      {
        title: '拓展名',
        dataIndex: 'extensions',
        width: 80,
      },
      {
        title: '备注',
        dataIndex: 'remark',
        width: 120,
      },
      {
        title: '创建时间',
        dataIndex: 'createTime',
        width: 120,
      },
      {
        title: '更新时间',
        dataIndex: 'updateTime',

        width: 120,
      },
      {
        title: '操作',
        key: 'action',
      },
    ],
)


const fileList = ref([]);


const beforeUpload = file => {
  fileList.value = [...(fileList.value || []), file];
  return false;
};
const handleRemove = file => {
  const index = fileList.value.indexOf(file);
  const newFileList = fileList.value.slice();
  newFileList.splice(index, 1);
  fileList.value = newFileList;
};
const uploading = ref(false)

function calculateFileSize(size) {
  if (typeof size !== 'number') {
    return '数据类型不合法'
  }
  if (size === 0) {
    return '0 B'
  }
  const units = ['B', 'KiB', 'MiB', 'GiB', 'TiB'];
  const i = Math.floor(Math.log2(size) / 10) || 0;
  const unit = units[i] || 'TiB';
  return `${(size / Math.pow(1024, i)).toFixed(2)} ${unit}`;
}

function getFileExtension(filename) {
  // 使用正则表达式从文件名中提取扩展名
  const match = filename.match(/\.[^.\\/]+$/);
  if (match) {
    return match[0].slice(1);
  } else {
    return '未知';
  }
}

function handleUpload() {

  uploading.value = true;
  fileList.value.forEach(async (item) => {
    const now = dayjs().format('YYYY-MM-DD HH:mm:ss')
    dataSource.value.push({
      id: item.uid,
      name: item.name,
      fileSize: calculateFileSize(item.size),
      extensions: getFileExtension(item.name),
      remark: '',
      createTime: now,
      updateTime: now,
      raw: item.originFileObj,
    })
  })
  fileList.value = [];
  uploading.value = false;
  message.success('文件上传成功');
}

const currentRecord = ref(null);
const modalType = ref(false);

function openViewModal(record) {
  modalType.value = 'view'
  currentRecord.value = record
}

function removeRecord(record) {
  dataSource.value.splice(dataSource.value.findIndex((item) => item.id === record.id), 1);
}

function editRecord(record) {
  currentRecord.value = record
  modalType.value = 'edit'
}

function handleEdit(formModel) {
  console.log('formModel', formModel)
  const editIndex = dataSource.value.findIndex((item) => item.id === currentRecord.value.id)
  const prev = dataSource.value[editIndex]

  dataSource.value[editIndex] = {
    ...prev,
    name: formModel.name,
    remark: formModel.remark,
  }
  console.log(dataSource.value);
}


</script>

<template>
  <ViewModal v-model:modalType="modalType"
             :record="currentRecord"
             @edit="handleEdit"
  />
  <div class="flex items-center gap-2">
    <a-upload
        v-model:file-list="fileList"
        name="file"
        multiple
        :show-upload-list="false"
        :file-list="fileList" :before-upload="beforeUpload" @remove="handleRemove"
    >
      <div class="flex items-center gap-4">
        <a-button class="flex items-center">
          <upload-outlined></upload-outlined>
          选择文件
        </a-button>
        <a-button
            type="primary"
            :disabled="fileList.length === 0"
            :loading="uploading"
            @click.stop="handleUpload"
        >
          {{ uploading ? '上传者' : '开始上传' }}
        </a-button>
      </div>
    </a-upload>
  </div>
  <div class="flex items-center my-8 justify-between">
    <div class="flex items-center gap-0.5">
      <div class="w-40">文件名称:</div>
      <a-input></a-input>
      <a-button :icon="h(SearchOutlined)" class="flex items-center">查询</a-button>
    </div>
    <div class="flex items-center gap-4">
      <a-button type="primary" :icon="h(PlusOutlined)" class="flex items-center">
        选取Excel
      </a-button>
      <div class="bg-button upload">
        <a-button ghost :icon="h(UploadOutlined)" class="flex items-center">
          上传Excel
        </a-button>
      </div>
    </div>
  </div>
  <a-table :dataSource="dataSource"
           :columns="columns"
           :scroll="{y:'calc(100vh - 345px)'}"
           :pagination="false"
  >
    <template #bodyCell="{ column, record }">
      <template v-if="column.key === 'action'">
        <div class="action-buttons">
          <a-button @click="openViewModal(record)">查看</a-button>
          <div class="bg-button info">
            <a-button ghost @click="editRecord(record)">编辑</a-button>
          </div>
          <a-button danger @click="removeRecord(record)">删除</a-button>
          <div class="bg-button warn">
            <a-button ghost>添加并注册</a-button>
          </div>
        </div>
      </template>
    </template>
  </a-table>
</template>

<style scoped>
.action-buttons {
  display: flex;
  gap: 4px;
}

.bg-button {
  padding: 0;
  display: inline-block;
  border-radius: 6px
}

.bg-button.info {
  background: #909399;
}

.bg-button.warn {
  background: #e6a23c;
}

.bg-button.upload {
  background: #67c23a;
}
</style>
