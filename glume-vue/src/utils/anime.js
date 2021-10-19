import anime from 'animejs'

export function anime404() {
  anime({
    targets: ".row svg",
    translateY: 10,
    autoplay: true,
    loop: true,
    easing: 'easeInOutSine',
    direction: 'alternate'
  });
  anime({
    targets: '#zero',
    translateX: 10,
    autoplay: true,
    loop: true,
    easing: 'easeInOutSine',
    direction: 'alternate',
    scale: [{value: 1}, {value: 1.4}, {value: 1, delay: 250}],
    rotateY: {value: '+=180', delay: 200},
  });
}

export function lockScreen() {
  const staggerVisualizerEl = document.querySelector('.stagger-visualizer');
  const fragment = document.createDocumentFragment();
  const numberOfElements = 81;
  for (let i = 0; i < numberOfElements; i++) {
    let div = document.createElement('div')
    div.setAttribute("class","animeDiv")
    // div.setAttribute("style","width: 64px !important;height: 128px !important;" +
    //   " border: 2px solid radial-gradient(at 50% -20%,#908392,#0d060e);" +
    //   "box-sizing: inherit;background-color: #141019;background: radial-gradient(at 20% -50%,#0d060e, #908392);")
    fragment.appendChild(div);
  }
  staggerVisualizerEl.appendChild(fragment);
  const staggersAnimation = anime.timeline({
    targets: '.stagger-visualizer div',
    easing: 'easeInOutSine',
    delay: anime.stagger(50),
    loop: true,
    autoplay: false,
    duration: 800,
    loopComplete: (a) => console.log('end'),
    //update: () => console.log(staggersAnimation.progress)
  })
    .add({
      scale: anime.stagger([2.5, 2], {from: 'center',grid: [9, 9]}),
      translateX: anime.stagger([-100, 100]),
      rotate: anime.stagger([-45, 45]), // 旋转将在-45deg到45deg之间均匀分布在所有元素之间
      easing: 'easeInOutCirc',
      delay: anime.stagger(10, {from: 'center',start: 500})// 每个元素的延迟增加10毫秒。延迟从500ms开始，然后每个元素增加100ms。
    })
    .add({
      scale: anime.stagger([1.5, 0.5], {from: 'center'}),
      translateY: anime.stagger([-200,200], 100),
      rotate: anime.stagger([10, -10], {from: 'last'}),
      delay: anime.stagger(10, {from: 'center', grid: [9, 9]}),
    })
    .add({
      scale: anime.stagger([1.5, 0.5], {from: 'center'}),
      translateY: anime.stagger([200,-200], 100,),
      rotate: anime.stagger([-10, 10], {from: 'last'}),
      delay: anime.stagger(10, {from: 'center', grid: [9, 9]}),
    })
    .add({
      scale: anime.stagger([2.5, 1], {from: 'center', easing: 'easeInOutCirc'}),
      translateX: anime.stagger([-200, 200]),
      translateY: anime.stagger([-200, 200]),
      rotate: 0,
      delay: anime.stagger(1, {from: 'last'})
    })
    .add({
      rotate: anime.stagger(2, {from: 'center', direction: 'reverse', easing: 'easeInSine'}),
      translateX: 0,
      translateY: 0,
      delay: anime.stagger(10, {from: 'center'})
    })
    .add({
      scale: anime.stagger([2.5, 2], {grid: [9, 9], axis: 'y'}),
      translateY: anime.stagger([-120, 200], {grid: [9, 9], axis: 'y'}),
      rotate: 0,
      delay: anime.stagger(1, {from: 'last'})
    })
    .add({
      scale: anime.stagger([2.5, 2], {grid: [9, 9], axis: 'y'}),
      translateY: anime.stagger([-120, 200], {grid: [9, 9], axis: 'y'}),
      translateX: anime.stagger([120, -200], {grid: [9, 9], axis: 'y'}),
      rotate: 0,
      delay: anime.stagger(1, {from: 'last'})
    })
    .add({
      scale: anime.stagger([1, 1], {from: 'center'}),
      translateX: () => anime.random(-6, 6),
      translateY: () => anime.random(-6, 6),
      rotate: anime.stagger([-60, 60], {from: 'first'}),
      delay: anime.stagger(10, {from: 'first', grid: [9, 9]}),
    })
    .add({
      scale: anime.stagger([2.2, 0.2], {from: 'center'}),
      translateX: anime.stagger([-20,-20], 100),
      translateY: anime.stagger([-200,-200], 100),
      rotate: anime.stagger([-120, 120], {from: 'last'}),
      delay: anime.stagger(10, {from: 'center', grid: [9, 9]}),
    })
    .add({
      scale: anime.stagger([2.2, 0.5], {from: 'center'}),
      translateX: anime.stagger([-10,-10], 100),
      translateY: anime.stagger([-200,-200], 100),
      rotate: anime.stagger([180, -180], {from: 'last'}),
      delay: anime.stagger(10, {from: 'center', grid: [9, 9]}),
    })
    .add({
      translateX: anime.stagger('1rem', {grid: [9, 9], from: 'center', axis: 'x'}),
      //translateY: anime.stagger('1rem', {grid: [9, 9], from: 'center', axis: 'y'}),
      delay: anime.stagger(200, {grid: [9, 9], from: 'center', direction: 'reverse'})
    })
    .add({
      scale: anime.stagger([1, 1], {from: 'center'}),
      translateX:  anime.stagger(0, {from: 'center'}),
      translateY: anime.stagger(6, {from: 'center', direction: 'reverse'}),
      rotate: 0,
      delay: anime.stagger(1, {from: 'center', grid: [9, 9]}),
    })
    .add({
      translateX: anime.stagger([25, -25], {from: 'first'}),
      translateY: 0,
      rotate: anime.stagger([40, -40], {from: 'first'}),
      delay: anime.stagger(10, {from: 'first'}),
    })
    .add({
      translateY: anime.stagger([-240, 240]),
      rotate: () => anime.random(-100, 100),
      scale: anime.stagger([1, 3], {from: 'center'}),
      delay: anime.stagger(10, {from: 0}),
    })
    .add({
      translateX: 0,
      translateY: 0,
      scale: 1,
      rotate: 360,
      duration: 2000,
      delay: 0,
    });
    staggersAnimation.play();
}
