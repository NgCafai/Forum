$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	// 根据 id 获取帖子的标题和内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();

	// 发送异步请求
	$.post(
		CONTEXT_PATH + "/post/add",
		{"title": title, "content": content},
		function (data) {
			console.log(data);
			data = $.parseJSON(data);
			console.log(data.code);
			console.log(data.msg);
			// 将消息插入到提示框的文本中
			$("#hintBody").text(data.msg);
			// 显示提示框
			$("#hintModal").modal('show');
			// 2秒后,自动隐藏提示框
			setTimeout(function () {
				$("#hintModal").modal("hide");
				// 如果发帖成功，则刷新页面
				if (data.code == 0) {
					console.log("更新页面");
					window.location.reload();
				}
			}, 2000);
		}
	);
}