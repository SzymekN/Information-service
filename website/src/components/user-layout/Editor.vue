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
const category = ref("")
const acceptance = ref("")
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
    articleId = JSON.parse(sessionStorage.getItem("articleToEdit")).id
    acceptance.value = route.query["acceptance"]
  }
  role.value = atob(jsCookie.get("ROLE"))
  console.log(role.value)
})

const updateArticle = async (url, method, body, redirect = false) =>{
  try {
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
      if (method == "POST" || method == "PUT"){
        toast.success("Artykuł zapisany")
      } else if (method == "DELETE"){
        toast.success("Artykuł przeniesiony")
      }
      if (redirect == true){
        if (role.value == "ROLE_JOURNALIST"){
          console.log("redirecting")
          router.push({ name: 'draftsList' })
        }
        else{
          console.log("redirecting")
          router.push({ name: 'articlesList' })
        }
      }
    }
  } catch (error) {
    console.log(error);
  }
}

const handleEdit = async () =>{

  var method;
  var body = {
    "title": title.value,
    "content": content.value,
  };
  var url = '/editorial/draft';

  if (acceptance.value == ""){
    toast.error("Wybierz status artykułu")
    return;
  }
  if (role.value == "ROLE_JOURNALIST"){
    url = '/editorial/draft';
  }
  else
  {
    url = '/editorial/correct';
  }
  var updateURL = url;

  if (acceptance.value == "ready" && (role.value == "ROLE_REDACTOR" || role.value == "ROLE_JOURNALIST")) {
    if (role.value == "ROLE_REDACTOR"){
      url += '/reject'
    }
    method = "DELETE"
    url += `?id=${articleId}`
  }
  else if(acceptance.value == "approved"){
    if (category.value == ""){
      toast.error("Wybierz kategorię")
      return;
    }
    url += '/accept'
    method = "DELETE"
    url += `?id=${articleId}&category=${category.value}`
  }
  else if (articleId == 0){
    method = "POST"
  }
  else{
    method = "PUT"
    body["id"] = articleId
    if (role.value != "ROLE_JOURNALIST"){
      if (acceptance.value == "corrected")
        body["isCorrected"] = 1
      else 
        body["isCorrected"] = 0
    }
  }

  if (method == "DELETE"){
    if (role.value == "ROLE_JOURNALIST" && articleId == 0){
      await updateArticle(url, "POST", body, true);
    }
    else{
      body["id"] = articleId
      if (role.value != "ROLE_JOURNALIST"){
        if (acceptance.value == "corrected")
        body["isCorrected"] = 1
        else 
        body["isCorrected"] = 0
      }
      await updateArticle(updateURL, "PUT", body);
    }
  }

  updateArticle(url, method, body, true);
}

</script>

<template>

  <h1>Edytor</h1>
  <div class="properties">
    <label for="title">Tytuł artykułu:</label>
    <input type="text" v-model="title" placeholder="Tytuł artykułu" class="title_input"><br/>
    
    <div  v-if="role=='ROLE_REDACTOR'">
      <label for="category">Wybierz kategorię:</label>
      <select v-model="category" class="category_input">
        <option value="politics">Polityka</option>
        <option value="technology">Technologia</option>
        <option value="business">Biznes</option>
        <option value="sport">Sport</option>
      </select><br/>
    </div>

    <label for="category">Oznacz jako:</label>
    <select v-model="acceptance" class="category_input">
      <option value="draft" v-if="role=='ROLE_JOURNALIST'">Szkic</option>
      <option value="ready" v-if="role=='ROLE_JOURNALIST' || role=='ROLE_REDACTOR' || role=='ROLE_CORRECTOR'">Gotowy</option>
      <option value="corrected" v-if="role=='ROLE_CORRECTOR' || role=='ROLE_REDACTOR'">Poprawiony</option>
      <option value="approved" v-if="role=='ROLE_REDACTOR'">Zaakceptowany</option>
    </select><br/>
    <button class="save_button" @click="handleEdit">Zapisz</button>
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

#editorContainer{
  background-color: white;
  margin-bottom: 1rem;
  box-shadow: 2px 2px 20px -10px;
}

#justText {
  background-color: white;
  margin-bottom: 1rem;
  box-shadow: 2px 2px 20px -10px;
  max-width: 100%;
  border-radius: 10px;
}

.properties {
  background: white;
  width: fit-content;
  padding: 1%;
  /* border: 1px solid rgba(128, 128, 128, 0.5); */
  box-shadow: 2px 2px 20px -10px;
  border-radius: 10px;
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