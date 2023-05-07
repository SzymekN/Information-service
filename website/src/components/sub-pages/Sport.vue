<template>
  <div>
    <RouterView/>
  </div>
</template>

<script>
import { ref } from "vue";
import LeagueTable from "@/components/api-leagues/LeagueTable.vue";
import ArticleSection from "@/components/articles/ArticleSection.vue";
import LeagueTop5 from "../api-leagues/LeagueTop5.vue";

export default {
  components: {
    LeagueTop5,
    ArticleSection,
  },
  setup() {
    // add any necessary setup logic here
    return {};
  },
};
</script>