<script setup>
import TheRightSection from "@/components/api-weather/TheRightSection.vue";
import ArticleSection from "@/components/articles/ArticleSection.vue";
import {Toaster, toast } from 'vue-sonner'
import { onMounted } from "vue";
import jsCookie from "js-cookie";


onMounted (() => {

  // toasting info about login and logout
  if (!sessionStorage.getItem("loginMonitShown"))
    sessionStorage.setItem("loginMonitShown", "false");

  if (sessionStorage.getItem("loginMonitShown") == "false" && jsCookie.get("ROLE")){
    toast.success("Zalogowano pomyślnie");
    sessionStorage.setItem("loginMonitShown", "true");
    sessionStorage.setItem("logoutMonitShown", "false");
  }

  if (sessionStorage.getItem("logoutMonitShown") == "false" && !jsCookie.get("ROLE")){
    toast.success("Wylogowano pomyślnie");
    sessionStorage.setItem("logoutMonitShown", "true");
    sessionStorage.setItem("loginMonitShown", "false");
  }

})


</script>

<template>
  <Toaster richColors position="top-center"/>
  <div id="content_container">
    <div class="section_content">

      <p class = "section_title">O TYM SIĘ MÓWI</p>
      <hr>
        <ArticleSection/>
    </div>

  </div>

  <section>
    <TheRightSection />
  </section>

</template>

<style scoped>

#content_container {
  width: 80%;
  float: left;
}
.section_content {
  float: left;
}

section {
  box-shadow: 2px 2px 20px -10px;
}

@media (max-width: 640px){
  .section_content:first-child {
    margin-top: 6rem;
  }
  section{
    display: none;
  }
  #content_container {
    width: 100%;
    margin: 1rem 0;
  }
}

</style>