<script setup>
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import '@vueup/vue-quill/dist/vue-quill.core.css';
import { ref, onMounted, defineProps} from 'vue'
import { useRoute } from 'vue-router'


const route = useRoute();
const content = ref('')

const props = defineProps({
  content: { type: String, default: 'param' }
})

onMounted(() => {
    console.log(route.query["id"]);
    let index = route.query["id"]
    console.log(JSON.parse(localStorage.getItem("allArticles") || [])[index])
    content.value = JSON.parse(localStorage.getItem("allArticles") || [])[index].content
})

</script>

<template>

    <div id="justText" class="content ql-editor" v-html="content"></div>

</template>