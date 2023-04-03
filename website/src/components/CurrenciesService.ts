import { ref,Ref } from "vue";
import { ExchangeTable } from "./CurrenciesModel";

const nbpUrl = 'https://api.nbp.pl/api/exchangerates/tables/a/last/2';
const exchangeTable = ref<ExchangeTable[]>([]);

export function useCurrencies() {
  return {
    exchangeTable,
    fetchCurrencies,
  }
}
async function fetchCurrencies(): Promise<Ref<ExchangeTable[]>> {
  try {
    const response = await fetch(nbpUrl);
    const exchangeTableArray = await response.json();
    exchangeTable.value = exchangeTableArray;
    console.log(exchangeTable.value)
    return exchangeTable;
  } catch (error) {
    console.error(error);
    return exchangeTable;
  }
}
