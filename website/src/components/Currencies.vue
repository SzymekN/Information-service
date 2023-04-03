<template>
  <div>
    <div v-if="cards && cards.length">
      <SlidingArray :cards="cards"></SlidingArray>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";
import SlidingArray from "./SlidingArray.vue";
import { useCurrencies } from './CurrenciesService'

export default {
  components: {
    SlidingArray
  },
  setup() {
    const { exchangeTable, fetchCurrencies } = useCurrencies();
    const cards = ref([]);

    const fetchExchangeTable = async () => {
      await fetchCurrencies();
      cards.value = exchangeTable.value[0]?.rates;
    };

    fetchExchangeTable();

    return {
      cards,
    };
  }
};
</script>