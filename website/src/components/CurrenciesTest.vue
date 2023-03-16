<template>
  <div class="slider-container">
    <div class="slider" v-bind:style="{ transform: 'translateX(' + translateX + 'px)' }">
      <div v-for="(item, index) in items" v-bind:key="index" class="slider-item">{{ item.title }} {{ item.value }}</div><div v-for="(item, index) in items" v-bind:key="index" class="slider-item">{{ item.title }} {{ item.value }}</div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'

export default {
  setup() {
  const items = ref([
    { title: 'Item 1', value: '1' },
    { title: 'Item 2', value: '2' },
    { title: 'Item 3', value: '3' },
    { title: 'Item 4', value: '4' },
    { title: 'Item 5', value: '5' },
    { title: 'Item 6', value: '6' },
    { title: 'Item 7', value: '7' },
  ])

  // Duplicate first and last items
  //items.value.unshift(items.value[items.value.length - 1])
  //items.value.push(items.value[1])

  const containerWidth = ref(0)
  const translateX = ref(0)
  const scrollDistance = ref(2)
  const scrollDelay = ref(1)

  const updateContainerWidth = () => {
    const sliderItems = document.querySelectorAll('.slider-item')
    const containerWidthValue = Array.from(sliderItems).reduce((total, el) => {
      const computedStyle = window.getComputedStyle(el)
      const marginLeft = parseFloat(computedStyle.marginLeft)
      const marginRight = parseFloat(computedStyle.marginRight)
      return total + el.offsetWidth + marginLeft + marginRight
    }, 0)
    containerWidth.value = containerWidthValue
  }

  const scrollItems = () => {
  const distance = scrollDistance.value
  let currentX = translateX.value
  const containerWidthValue = containerWidth.value

  const sliderItems = document.querySelectorAll('.slider-item')
  const lastVisibleItemIndex = Math.floor((-currentX + window.innerWidth) / sliderItems[0].offsetWidth)

  if (lastVisibleItemIndex >= items.value.length - 1) {
    items.value = [...items.value,...items.value]
    updateContainerWidth()
  }

  if (currentX <= -containerWidthValue) {
    currentX += containerWidthValue
  }

  translateX.value = currentX - distance

  setTimeout(scrollItems, scrollDelay.value)
}



  onMounted(() => {
    updateContainerWidth()
    setTimeout(scrollItems, scrollDelay.value)
  })

  return { items, containerWidth, translateX }
},

}
</script>

<style>
.slider-container {
  width: 100%;
  overflow: hidden;
}

.slider {
  display: flex;
}

.slider-item {
  color: white;
  text-align: center;
  padding: 20px;
  font-size: 1.5em;
  margin: 0 10px;
}

.active {
  background-color: white;
  color: black;
}
</style>
