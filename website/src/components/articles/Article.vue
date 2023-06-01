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

    <h1 class="done-article">{{title}}</h1>
    <div id="justText" class="content ql-editor" v-html="content"></div>
  <div class="suggested-articles">
    <RecommendedArticles></RecommendedArticles>
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