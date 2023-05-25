<script setup>
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import '@vueup/vue-quill/dist/vue-quill.core.css';
import { ref, reactive, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { toast } from "vue-sonner";
import jsCookie from "js-cookie";
import router from "@/router";

const quillEditor = ref()
const content = ref("")
const title = ref()
const category = ref("politics")
const acceptance = ref("draft")
const options = reactive({
    theme: 'snow',
    placeholder: 'Zacznij pisać artykuł...',
})

const role = ref("")
var articleId = 0;

onMounted(() => {
  console.log("mounted")
  let route = useRoute();
  let redirected = route.query["redirected"]
  if (redirected != undefined){
    content.value = JSON.parse(sessionStorage.getItem("articleToEdit")).content
    title.value = JSON.parse(sessionStorage.getItem("articleToEdit")).title
    // category.value = JSON.parse(sessionStorage.getItem("articleToEdit")).category
    articleId = JSON.parse(sessionStorage.getItem("articleToEdit")).id
  }
  role.value = atob(jsCookie.get("ROLE"))
  console.log(role.value)
})

const saveArticle = async () =>{
  try {
    var method;
    var body;
    var url = '/editorial/draft';

    if (acceptance.value == "ready") {
      method = "DELETE"
      body = {}
      url += `?id=${articleId}`
    }
    else if (articleId == 0){
      method = "POST"
      body = {
        "title": title.value,
        "content": content.value,
        // "category": category.value,
      }
    }
    else{
      method = "PUT"
      body = {
        "id": articleId,
        "title": title.value,
        "content": content.value,
        // "category": category.value,
      }
    }
    const response = await fetch(url, {
      method: method,
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body)
    });
    if (!response.ok) {
      const text = await response.json();
      for (const [key, value] of Object.entries(text)) {
        setTimeout(() => toast.error(`${key}: ${value}`), 10)
      }
      setTimeout(() => toast.error("Wystąpił błąd podczas zapisywania"), 100)
    }
    else{
      toast.success("Artykuł zapisany")
      router.push({ name: 'draftsList' })
    }
  } catch (error) {
    console.log(error);
  }
}

</script>

<template>

  <h1>Edytor</h1>
  <div class="properties">
    <label for="title">Tytuł artykułu:</label>
    <input type="text" v-model="title" placeholder="Tytuł artykułu" class="title_input"><br/>
    <label for="category">Wybierz kategorię:</label>
    <select v-model="category" class="category_input">
      <option value="politics">Polityka</option>
      <option value="business">Biznes</option>
      <option value="business">Sport</option>
    </select><br/>
    <label for="category">Oznacz jako:</label>
    <select v-model="acceptance" class="category_input">
      <option value="draft" v-if="role=='ROLE_JOURNALIST'">Szkic</option>
      <option value="ready" v-if="role=='ROLE_JOURNALIST' || role=='ROLE_REDACTOR' || role=='ROLE_CORRECTOR'">Gotowy</option>
      <option value="corrected" v-if="role=='ROLE_CORRECTOR'">Poprawiony</option>
      <option value="approved" v-if="role=='ROLE_REDACTOR'">Zaakceptowany</option>
    </select><br/>
    <button class="save_button" @click="saveArticle">Zapisz</button>
    <br/><br/>
  </div>
  <h1 class="title">{{ title }}</h1>
  <div id="editorContainer">
    <QuillEditor v-model:content="content" :options="options" toolbar='full' ref="quillEditor" content-type="html"/>
  </div>

<!--     Preview div - ql-editor class is needed to have styling as in the editor-->
    <p class = "section_title">PODGLĄD ARTYKUŁU</p>
  <h1 class="title">{{ title }}</h1>
    <div id="justText" v-html="content" class="content ql-editor"></div>

</template>


<style scoped>
 .section_title {
   font-size: 1.5rem;
   font-weight: 400;
   margin: 1rem 0;
 }

 #editorContainer,
 #justText {
   background-color: white;
   margin-bottom: 1rem;
 }

 .properties {
   background: white;
   width: fit-content;
   padding: 1%;
   border: 1px solid rgba(128, 128, 128, 0.5);
 }
 label {
   margin: 10px;
 }
 input {
   padding: 10px;
   font-size: 0.8rem;
   margin: 2%;
   border: none;
   background: rgba(238, 246, 253, 0.7);
   border-radius: 10px;
   width: 96%;
 }

 select {
   font-size: 0.8rem;
   margin: 2%;
   border: none;
   background: rgba(240, 248, 255);
   border-radius: 10px;
   width: 96%;
   height: 3vh;
 }

 button {
   margin: 5% 2%;
   padding: 3%;
   background: rgb(238, 246, 253);
   border: none;
   width: 96%;
   font-size: 1rem;
 }

 button:hover {
   background-color: #c8d8f1;
 }

</style>