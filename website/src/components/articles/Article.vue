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

    <h1 class="done-article">{{title}}</h1>
    <div id="justText" class="content ql-editor" v-html="content"></div>

  <div class="suggested-articles">
    Miejsce na proponowane artykuły
  </div>
</template>

<style>
.done-article {
  text-transform: uppercase;
  margin-left: 2%;
}
.suggested-articles {
  width: 23%;
  margin-left: 2%;
  float: right;
  min-height: 100px;
  background: white;
  border-radius: 10px;
  padding: 2%;
  box-shadow: 2px 2px 20px -10px;
}
.content > h2, .content > h1 {
  width: 100%;
  margin-bottom: 1%;
}

.content > p > img {
  max-width: 50%;
  margin: 2% 25% 2% 25%;
}

.content {
  background: white;
  border-radius: 10px;
  width: 75%;
  float: left;
  padding: 2%;
  box-shadow: 2px 2px 20px -10px;
}

</style>