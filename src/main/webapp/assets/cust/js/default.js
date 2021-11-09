;(function() {
  $.fn.topMainSlider = function() {
    return this.each(function() {
      var $this = $(this);
      var $itemList = $this.find('.items');
      var $item = $itemList.find('li');
      var itemLength = $item.size();
      var isLoopable = itemLength > 1;
      var $topBgImg = $('.topBgWrap').find('li');

      var currentIndex;
      var beforeIndex;
      var switcher = function(e) {
        $topBgImg
          .eq(beforeIndex)
          .removeClass('before')
          .end()
          .eq(currentIndex)
          .addClass('before')
          .removeClass('current')
          .end()
          .eq(e.activeIndex % e.loopedSlides)
          .addClass('current');

        beforeIndex = currentIndex;
        currentIndex = e.activeIndex % e.loopedSlides;
      };

      topBannerSlider = new Swiper($this[0], {
        wrapperClass: 'items',
        slideClass: 'item',
        slidesPerView: 'auto',
        loopedSlides: itemLength,
        loop: isLoopable,
        autoplay: 3000,
        autoplayDisableOnInteraction: false,
        onInit: switcher,
        onSlideChangeStart: switcher
      });
    });
  };

  $.fn.clock = function(opts) {
    opts = $.extend({
      mins: '.mins',
      hrs: '.hrs'
    }, opts);

    return this.each(function() {
      var $clock = $(this);
      var $clockShape = $clock.find('.clockShape');
      var $time = $clock.find('.time');
      var dataTime = $time.attr('data-time');

      var $hands = {
        mins: $clockShape.find(opts.mins),
        hrs: $clockShape.find(opts.hrs)
      };

      var clockUpdate = function(data) {
        $.each(data.deg, function(key, value) {
          if (value === 0) {
            value = 360;

            $hands[key].one('transitionend', function() {
              var $this = $(this);

              $this
              .removeClass('running')
              .css({
                '-webkit-transform': 'rotate(0)'
              });

              setTimeout(function() {
                $this.addClass('running');
              }, 0);
            });
          }

          $hands[key].css({
            transform: 'rotate(' + value + 'deg)',
            WebkitTransform: 'rotate(' + value + 'deg)'
          });
        });
      };

      var date = new Date(dataTime.replace(/-/g, '/'));
      var mins = date.getMinutes();
      var hrs = date.getHours();

      clockUpdate({
        deg: {
          mins: mins * 360 / 60,
          hrs: (hrs + mins / 60 / 60) * 360 / 12
        }
      });
    });
  };

  $(document).ready(function() {
    $('.performListWrap').topMainSlider();
    $('.clock').clock();

    var todayPickSlider = new Swiper('.todayPickList', {
      wrapperClass: 'listWrap',
      slideClass: 'item',
      slidesPerView: 'auto',
      freeMode: true,
      spaceBetween: 5,
      preloadImages: false,
      onTouchStart: function () {
          jQuery(".todayPickList img").each(function () {
              if (jQuery(this).attr("data-src")) {
                  jQuery(this).attr("src", jQuery(this).attr("data-src"));
                  jQuery(this).removeAttr("data-src");
              }
          });
      }
    });


    var liveSlider = new Swiper('.slideList', {
      wrapperClass: 'listWrap',
      slideClass: 'item',
      slidesPerView: 'auto',
      freeMode: true,
      spaceBetween: 5,
      preloadImages: false,
      onTouchStart: function () {
          jQuery(".slideList img").each(function () {
              if (jQuery(this).attr("data-src")) {
                  jQuery(this).attr("src", jQuery(this).attr("data-src"));
                  jQuery(this).removeAttr("data-src");
              }
          });
      }
    });

  });
})();

/* Modal */
var pop = document.getElementById('mainPop');
var span = document.getElementById("menuPop");
var btn1 = document.getElementsByClassName("btn-today")[0];
var btn2 = document.getElementsByClassName("btn-close")[0];

	span.onclick = function() {
	pop.style.display = "block";
	}
	btn1.onclick = function() {
	pop.style.display = "none";
	}
	btn2.onclick = function() {
	pop.style.display = "none";
	}
window.onclick = function(event) {
	if (event.target == pop) {
	pop.style.display = "none";
	}
}
