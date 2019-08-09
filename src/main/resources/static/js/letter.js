$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
	$("#sendModal").modal("hide");

	var toName = $("#recipient-name").val();
	var content = $("#message-text").val();
	$.post(
		CONTEXT_PATH + "/message/send",
		{"toName":toName,"content":content},
		function(data) {
			data = $.parseJSON(data);
			$("#hintBody").text(data.msg);

			$("#hintModal").modal("show");
			// 2秒后,自动隐藏提示框
			setTimeout(function () {
				$("#hintModal").modal("hide");
				// 如果发送消息成功，则刷新页面
				if (data.code == 0) {
					console.log("更新页面");
					window.location.reload();
				}
			}, 2000);
		}
	);
}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}