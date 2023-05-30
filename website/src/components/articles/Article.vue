<script setup>
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import '@vueup/vue-quill/dist/vue-quill.core.css';
import { ref, onMounted} from 'vue'
import { useRoute } from 'vue-router'


const route = useRoute();
const content = ref('')
const title = ref('')

const props = defineProps({
  content: { type: String, default: 'param' },
  title: { type: String, default: 'Tytuł' },
})

onMounted(() => {
    let category = route.query["category"]
    let index = route.query["id"]
    console.log(JSON.parse(sessionStorage.getItem(category) || [])[index])
    content.value = JSON.parse(sessionStorage.getItem(category) || [])[index].content
    title.value = JSON.parse(sessionStorage.getItem(category) || [])[index].title
})

</script>

<template>

    <h1>{{title}}</h1>
    <div id="justText" class="content ql-editor" v-html="content"></div>

</template>