(window.kakao = window.kakao || {}),
  (window.kakao.maps = window.kakao.maps || {}),
  window.daum && window.daum.maps
    ? (window.kakao.maps = window.daum.maps)
    : ((window.daum = window.daum || {}),
      (window.daum.maps = window.kakao.maps)),
  (function () {
    function a() {
      if (E.length) {
        t(I[E.shift()], a).start();
      } else e();
    }
    function t(a, t) {
      var e = document.createElement("script");
      return (
        (e.charset = "utf-8"),
        (e.onload = t),
        (e.onreadystatechange = function () {
          /loaded|complete/.test(this.readyState) && t();
        }),
        {
          start: function () {
            (e.src = a || ""),
              document.getElementsByTagName("head")[0].appendChild(e),
              (e = null);
          },
        }
      );
    }
    function e() {
      for (; c[0]; ) c.shift()();
      o.readyState = 2;
    }
    var o = (kakao.maps = kakao.maps || {});
    if (void 0 === o.readyState) (o.onloadcallbacks = []), (o.readyState = 0);
    else if (2 === o.readyState) return;
    (o.VERSION = {
      ROADMAP: "2103dor",
      ROADMAP_SUFFIX: "",
      HYBRID: "2103dor",
      SR: "3.00",
      ROADVIEW: "7.00",
      ROADVIEW_FLASH: "200402",
      BICYCLE: "6.00",
      USE_DISTRICT: "2103dor",
      SKYVIEW_VERSION: "160114",
      SKYVIEW_HD_VERSION: "160107",
    }),
      (o.RESOURCE_PATH = {
        ROADVIEW_AJAX:
          "//t1.daumcdn.net/roadviewjscore/core/css3d/200204/standard/1580795088957/roadview.js",
        ROADVIEW_CSS:
          "//t1.daumcdn.net/roadviewjscore/core/openapi/standard/210215/roadview.js",
      });
    for (
      var n,
        r = "https:" == location.protocol ? "https:" : "http:",
        s = "",
        i = document.getElementsByTagName("script"),
        d = i.length;
      (n = i[--d]);

    )
      if (/\/(beta-)?dapi\.kakao\.com\/v2\/maps\/sdk\.js\b/.test(n.src)) {
        s = n.src;
        break;
      }
    i = null;
    var c = o.onloadcallbacks,
      E = ["v3"],
      S = "",
      I = {
        v3: r + "//t1.daumcdn.net/mapjsapi/js/main/4.3.2/kakao.js",
        services:
          r + "//t1.daumcdn.net/mapjsapi/js/libs/services/1.0.2/services.js",
        drawing:
          r + "//t1.daumcdn.net/mapjsapi/js/libs/drawing/1.2.5/drawing.js",
        clusterer:
          r + "//t1.daumcdn.net/mapjsapi/js/libs/clusterer/1.0.9/clusterer.js",
      },
      _ = (function (a) {
        var t = {};
        return (
          a.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (a, e, o) {
            t[e] = o;
          }),
          t
        );
      })(s);
    (S = _.appkey), S && (o.apikey = S), (o.version = "4.3.2");
    var R = _.libraries;
    if ((R && (E = E.concat(R.split(","))), "false" !== _.autoload)) {
      for (var d = 0, l = E.length; d < l; d++)
        !(function (a) {
          a &&
            document.write('<script charset="UTF-8" src="' + a + '"></script>');
        })(I[E[d]]);
      o.readyState = 2;
    }
    o.load = function (t) {
      switch ((c.push(t), o.readyState)) {
        case 0:
          (o.readyState = 1), a();
          break;
        case 2:
          e();
      }
    };
  })();
