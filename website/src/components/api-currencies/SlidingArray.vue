<template>
  <div class="react-horizontal-scrolling-menu--wrapper">
    <div class="react-horizontal-scrolling-menu--scroll-container1"
  ref="scrollContainer1"
  :style="{ '--play': 'running', '--direction': 'normal', '--duration': duration/2 + 's', '--delay': '0s', '--iteration-count': '1' }"
  @animationend="onAnimationEnd">
      <div class="react-horizontal-scrolling-menu--item" v-for="(card, index) in cards" :key="index">
        <a class="nts-item">
          <span class="nts-breakable">{{ card.code }}</span>
          <span class="nts-value">{{ card.mid }}</span>
          <span :class="card.changeClass">{{ card.change }}</span>
        </a>
      </div>
    </div>
    <div class="react-horizontal-scrolling-menu--scroll-container2" ref="scrollContainer2"   
    :style="{'--play': 'running','--direction': 'normal','--duration': duration +'s','--delay': '0s','--iteration-count': 'infinite'
    }">
      <div class="react-horizontal-scrolling-menu--item" v-for="(card, index) in cards" :key="'second-' + index">
        <a  class="nts-item">
          <span class="nts-breakable">{{ card.code }}</span>
          <span class="nts-value">{{ card.mid }}</span>
          <span :class="card.changeClass">{{ card.change }}</span>
        </a>
      </div>
    </div>
  </div>
</template>


<script>
import { ref, computed } from 'vue'
export default {
  props: {
      cards: {
        type: Array,
        default: null
      },
      prevCards:{
        type:Array,
        default:null
      },
      duration:{
        type: Number,
        default:1
      }
    },
  setup(props){
    // console.log('props.duration:', props.duration);
    const cards = ref(props.cards);
    const prevCards = ref(props.prevCards);
    const scrollContainer1 = ref("scrollContainer1");
    const onAnimationEnd = () => {
      if (scrollContainer1) {
        
        scrollContainer1.value.style.animation=`scrolling3 running normal ${props.duration}s 0s infinite linear`
        scrollContainer1.value.style.opacity="1"
      }
    }
    const calcDiff=async()=>{
      for (let i = 0; i < cards.value.length; i++) {
        var diff = ((cards.value[i].mid - prevCards.value[i].mid) / prevCards.value[i].mid) * 100;
        diff=diff.toFixed(2);
        if(diff>0){
          cards.value[i].change = "+"+diff+"% ▲"; 
          cards.value[i].changeClass="notoria profit";
          
        }
        else if(diff<0){
          cards.value[i].change = diff+"% ▼"; 
          cards.value[i].changeClass="notoria loss";
        }
        else{
          cards.value[i].change = diff+"% -"
          cards.value[i].changeClass="notoria even";
        }
      }
    }
    calcDiff();
  return {onAnimationEnd,scrollContainer1};
    }
}
</script>
<style>
.react-horizontal-scrolling-menu--wrapper {
  overflow: hidden;
  display: flex;
  flex-wrap: nowrap;
  /*background: linear-gradient(white, aliceblue, white);*/
  border-radius: 10px;
  background: white;
  box-shadow: 2px 2px 20px -10px;
  margin-right: 2%;
}
.react-horizontal-scrolling-menu--scroll-container1 {
  box-sizing: content-box;
  display: flex;
  --play: running;
  --direction: normal;
  --duration: 153.359s;
  --delay: 0s;
  --iteration-count: infinite;
  animation:scrolling var(--duration) var(--play) var(--delay) var(--iteration-count) linear;
}

.react-horizontal-scrolling-menu--scroll-container2 {

  box-sizing: content-box;
  display: flex;
  --play: running;
  --direction: normal;
  --duration: 153.359s;
  --delay: 0s;
  --iteration-count: infinite;
  animation:scrolling2 var(--duration) var(--play) var(--delay) var(--iteration-count) linear;
}


@keyframes scrolling {
  from { transform: translateX(calc(100%*0.5)); }
  to { transform: translateX(calc(-100%*0.5)); }
}

@keyframes scrolling2 {
  from { transform: translateX(calc(100%*0.5)); }
  to { transform: translateX(calc(-100%*1.5)); }
}
@keyframes scrolling3 {
  from { transform: translateX(calc(100%*1.5)); }
  to { transform: translateX(calc(-100%*0.5)); }
}
.react-horizontal-scrolling-menu--item {
  flex: 0 0 auto;
  margin-right: 20px;
}

.nts-item {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  text-align: center;
  padding: 15px 0;
  color: rgba(34, 34, 34, 0.80);
  text-decoration: none;
  white-space: nowrap;
}

.nts-breakable {
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100px;
  white-space: nowrap;
  color: rgba(34, 34, 34, 0.80);
}

.nts-value {
  font-size: 20px;
  font-weight: bold;
  margin-top: 5px;
  color: rgba(34, 34, 34, 0.80);
}

.notoria.loss {
  color: #ff0000;
}

.notoria.profit {
  color: #00b300;
}

.notoria.even {
  color: #4c4c4c;
}

</style>