import { ref ,toRaw} from "vue";

import { ExchangeTable } from "./CurrenciesModel";

const nbpUrl = 'https://api.nbp.pl/api/exchangerates/tables/a/';

const exchangeTable = ref<ExchangeTable[]>([]);

export function useCurrencies() {
  return {
    exchangeTable,
    fetchCurrencies,
  }
}

function formatDate(dateToFormat){

    let month = dateToFormat.getMonth()+1;
    if(month<10){
      month=month.toString().padStart(2, '0');
    }
    let day = dateToFormat.getDate();
    if(day<10){
      day=day.toString().padStart(2, '0');
    }
    const year = dateToFormat.getFullYear();

    return `${year}-${month}-${day}`;
}

async function fetchCurrencies(): Promise<void> {
    let today=formatDate(new Date());
    const currentDate = new Date();
    let tempResponse;
    let yesterday=formatDate(new Date(currentDate.setDate(currentDate.getDate()-1)));
    //console.log(today+yesterday)
    const response = await fetch(`${nbpUrl}${yesterday}/${today}`);
   // const exchangeTableArray: ExchangeTable[]= await response.json();
   exchangeTable.value=await response.json();
    // const responseJson=await response.json();
    // for (let i = 0; i < responseJson.length; i++){
    //   exchangeTable.value.push(JSON.stringify(responseJson[i]));
    // }

    console.log(exchangeTable.value[0])
}