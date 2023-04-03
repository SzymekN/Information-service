<template>
  <div class="carousel">
    <div class="inner" ref="inner" :style="innerStyles">
      <div class="card" v-if="cards" v-for="card in cards" :key="card"><a>
        {{ card.code }} {{ card.mid }}</a>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onBeforeUnmount, watch } from "vue";

export default {
  props: {
    cards: {
      type: Array,
      default: null
    },
    prevCards:{
      type:Array,
      default:null
    }
  },
  setup(props, { refs }) {
    const inner = ref("inner");
    const innerStyles = ref({});
    const carousel = ref(null);
    const intervalDuration=2000;
    var hover=false;
    let step = "";
    let transitioning = false;
    let intervalId = null;

    const setStep = () => {
      const innerWidth = inner.value.scrollWidth;
      const totalCards = props.cards.length;
      step = `${innerWidth / totalCards}px`;
      
    };

    const startInterval = () => {
      intervalId = setInterval(() => {
        next();
      }, intervalDuration);
    };

    const stopInterval = () => {
      clearInterval(intervalId);
      intervalId = null;
    };

    const next = () => {
  if (transitioning) return;
  if (!hover) {
    transitioning = true;
    moveLeft();
    const shiftedCard = props.cards.shift();
    afterTransition(() => {
      resetTranslate();
      props.cards.push(shiftedCard);
      transitioning = false;
    });
  }
};

    const moveLeft = () => {
      innerStyles.value = {
        transition: props.transition,
        transform: `translateX(-${step}) translateX(-${step})`
      };
    };

    const moveRight = () => {
      innerStyles.value = {
        transition: props.transition,
        transform: `translateX(${step}) translateX(-${step})`
      };
    };

    const afterTransition = (callback) => {
      const listener = () => {
        callback();
        inner.value.removeEventListener("transitionend", listener);
      };
      inner.value.addEventListener("transitionend", listener);
    };

    const resetTranslate = () => {
      innerStyles.value = {
        transition: "none",
        transform: `translateX(-${step})`
      };
    };


    onMounted(() => {

  inner.value.addEventListener("mouseleave", () => {
  hover = false;
});

inner.value.addEventListener("mouseenter", () => {
  hover = true;
});
  setInterval(() => {
    if (document.querySelector("body > p:hover") != null) {
        console.log("hovered");
    }
}, 10);
      setStep();
      resetTranslate();
      startInterval();
    });

    onBeforeUnmount(() => {
      stopInterval();
    });


  return {
    inner,
    innerStyles
  };
}
};
</script>

<style>
.carousel {
  width: 100%; /* set width to 100% of parent container */
  max-width: 800px; /* set a maximum width for the carousel */
  overflow: hidden;
}

.inner {
  display: flex; /* set display to flex */
  flex-wrap: nowrap; /* prevent the cards from wrapping to a new line */
  transition: transform 1000ms ease-out;
}

.card {
  flex: 1; /* make the width flexible based on the content */
  margin-right: 10px;
  height: 50px;
  background-color: #ffffff;
  color: rgb(0, 0, 0);
  border-radius: 4px;
  align-items: center;
  justify-content: center;
  display: flex;
  padding: 0 10px; /* add some padding to the left and right of the card */
  box-sizing: border-box; /* include the padding in the width of the card */
  text-align: center; /* center the text horizontally */
  display: inline-block;
}
.card-text {
  text-align: center;
  margin: auto;
}

</style>