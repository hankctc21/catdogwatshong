$(function() {
	const $head = $("head")[0];
	const ensureScript = (src) => {
		if ($('script[src="' + src + '"]').length === 0)
			$('<script>').attr("src", src).appendTo($head);
	};
	const ensureCss = (href) => {
		if ($('link[href="' + href + '"]').length === 0)
			$('<link rel="stylesheet">').attr("href", href).appendTo($head);
	};

	ensureScript("https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js");
	ensureScript("https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js");
	ensureScript("//cdn.jsdelivr.net/npm/sweetalert2@11");
	ensureCss("https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css");
	ensureCss("/css/main.css");
	ensureCss("https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.css");
}); 
