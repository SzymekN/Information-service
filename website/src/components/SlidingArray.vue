<template>
  <div class="carousel">
    <div class="inner" ref="inner" :style="innerStyles">
      <div class="card" v-if="cards" v-for="card in cards" :key="card">
        {{ card.mid }}
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onBeforeUnmount,watch } from "vue";

export default {
  props: {
    intervalDuration: {
      type: Number,
      default: 200
    },
    cards: {
      type: Array,
      // default: () => [1, 2, 3, 4, 5, 6, 7, 8]
      default: null
    }
  },
  setup(props) {
    const inner = ref(null);
    const innerStyles = ref({});
    let step = "";
    let transitioning = false;
    let intervalId = null;

    const setStep = () => {
      const innerWidth = inner.value.scrollWidth;
      const totalCards = props.cards.length;
      step = `${innerWidth / totalCards}px`;
    };
    console.log(props.intervalDuration)
    const startInterval = () => {
      intervalId = setInterval(() => {
        next();
      }, props.intervalDuration);
    };

    const stopInterval = () => {
      clearInterval(intervalId);
      intervalId = null;
    };

    const next = () => {
      if (transitioning) return;
      transitioning = true;
      moveLeft();
      afterTransition(() => {
        const card = props.cards.shift();
        props.cards.push(card);
        resetTranslate();
        transitioning = false;
      });
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
      setStep();
      resetTranslate();
      startInterval();

    });

    onBeforeUnmount(() => {
      stopInterval();
    });

    // watch(() => props.cards, (newVal, oldVal) => {
    //   if (newVal && !oldVal) {
    //     setStep();
    //     resetTranslate();
    //     startInterval();
    //   }
    // });

    return {
      inner,
      innerStyles
    };
  }
};
</script>

<style>
.carousel {
  width: 170px;
  overflow: hidden;
}
.inner {
  transition: transform 2s;
  white-space: nowrap;
}
.card {
  width: 40px;
  margin-right: 10px;
  display: inline-flex;
  height: 40px;
  background-color: #39b1bd;
  color: white;
  border-radius: 4px;
  align-items: center;
  justify-content: center;
}

button {
  margin-right: 5px;
  margin-top: 10px;
}
</style>