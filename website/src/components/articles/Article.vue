<script setup>
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import '@vueup/vue-quill/dist/vue-quill.core.css';
import  RecommendedArticles from './RecommendedArticles.vue'
import { ref, onMounted,watch} from 'vue'
import { useRoute,useRouter} from 'vue-router'


const route = useRoute();
const router = useRouter();
const content = ref('')
const title = ref('')

const props = defineProps({
  content: { type: String, default: 'param' },
  title: { type: String, default: 'Tytuł' },
})

const loadContent = () => {
  let category = route.query["category"];
  let index = route.query["id"];
  content.value = JSON.parse(sessionStorage.getItem(category) || [])[index].content;
  title.value = JSON.parse(sessionStorage.getItem(category) || [])[index].title;
};

onMounted(() => {
  let category = route.query["category"];
  let index = route.query["id"];
  content.value = JSON.parse(sessionStorage.getItem(category) || [])[index].content;
  title.value = JSON.parse(sessionStorage.getItem(category) || [])[index].title;
})

watch(
      () => route.query, // Watch for changes in the query parameters
      (newQuery, oldQuery) => {
        // Perform any necessary logic when the query parameters change
        //loadContent();
        router.go();
      }
    );
</script>

<template>
    <h1>{{title}}</h1>
    <div id="justText" class="content ql-editor" v-html="content"></div>
    <RecommendedArticles></RecommendedArticles>
</template>