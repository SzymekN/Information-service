<script setup>
import TheRightSection from "@/components/api-weather/TheRightSection.vue";
import ArticleSection from "@/components/articles/ArticleSection.vue";

</script>

<template>

  <div id="content_container">
    <div class="section_content">

      <p class = "section_title">O TYM SIĘ MÓWI</p>
      <hr>
        <ArticleSection/>
    </div>

    <div class="section_content">

      <p class = "section_title">EDUKACJA</p>
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
  /* margin-top: 7.5rem; */
}
.section_content {
  float: left;
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