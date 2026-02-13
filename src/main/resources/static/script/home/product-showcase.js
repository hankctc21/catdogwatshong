$(function() {
	renderShowcase("/products/newList?pageno=1", "#today-discovery", {
		limit: 4,
		imageClass: "icon_img",
		titleClass: "contents1_bold",
		sorter: function(a, b) { return (b.pno || 0) - (a.pno || 0); }
	});
	renderShowcase("/products/bestList?pageno=1", "#hot-products", {
		limit: 4,
		imageClass: "icon2_img",
		titleClass: "contents2_bold",
		sorter: function(a, b) { return (b.goodCnt || 0) - (a.goodCnt || 0); }
	});
});

function renderShowcase(url, targetSelector, options) {
	$.ajax(url).done(function(result) {
		const products = (result && result.content) ? result.content : [];
		const $target = $(targetSelector).empty();
		const sortedProducts = options.sorter ? products.sort(options.sorter) : products;
		const items = sortedProducts.slice(0, options.limit);

		if (items.length === 0) {
			$("<li>")
				.append($("<div>").addClass("contents2").text("등록된 상품이 아직 없습니다."))
				.appendTo($target);
			return;
		}

		items.forEach(function(product) {
			const imageName = encodeURIComponent(product.imageFileName || "__missing__");
			const imageSrc = "/products/image?imagename=" + imageName;
			const priceText = Number(product.price || 0).toLocaleString("ko-KR") + "원";

			const $li = $("<li>").appendTo($target);
			$("<div>").addClass(options.imageClass)
				.append(
					$("<img>")
						.attr("src", imageSrc)
						.attr("alt", product.name || "상품 이미지")
						.css({width: "150px", height: "150px", objectFit: "cover", cursor: "pointer"})
						.on("click", function() {
							location.href = "/product/read?pno=" + product.pno;
						})
				)
				.appendTo($li);

			$("<div>").addClass(options.titleClass).text(product.name || "이름 없음").appendTo($li);
			$("<div>").addClass("contents2").text(priceText).appendTo($li);
			$("<div>")
				.addClass("more")
				.css("cursor", "pointer")
				.text("MORE")
				.on("click", function() {
					location.href = "/product/read?pno=" + product.pno;
				})
				.appendTo($li);
		});
	});
}
