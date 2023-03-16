<script>
import { defineComponent } from 'vue'
import { useCurrencies } from './CurrenciesService';

export default{
    setup(){
        const{exchangeTable,fetchCurrencies}= useCurrencies();
        if(!exchangeTable.value?.currency){
            fetchCurrencies();
        }
        return{
            exchangeTable
        }
    }
}

</script>
<template>

</template>