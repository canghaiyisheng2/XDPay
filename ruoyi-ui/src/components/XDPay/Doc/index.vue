<template>
  <div>
    <svg-icon icon-class="question" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'RuoYiDoc',
  data() {
    return {
      url: 'https://github.com/canghaiyisheng2/XDPay/blob/master/README.md'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>