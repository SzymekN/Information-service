<template>
    <div>
      <div v-if="showTop5">
        <LeagueTop5 @track-top-5="handleTrackTop5" />
        <p class = "section_title">O TYM SIĘ MÓWI W SPORCIE</p>
        <hr>
        <ArticleSection/>
      </div>
      <div v-else>
        <LeagueTable @track-top-5="handleTrackTop5" />
      </div>
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
    LeagueTable,
    ArticleSection,
  },
  setup() {
    
    const showTop5 = ref(true);

    const handleTrackTop5 = () => {
      showTop5.value = !showTop5.value;
    }

    return {
      showTop5,
      handleTrackTop5,
    }
  },
};
</script>
