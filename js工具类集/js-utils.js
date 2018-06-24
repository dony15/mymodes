var interval;

function startSecond() {

    interval = window.setInterval("changeSecond()", 1000);

};

function changeSecond() {
    var second = document.getElementById("second");

    var svalue = second.innerHTML;

    svalue = svalue - 1;

    if (svalue == 0) {
        window.clearInterval(interval);
        location.href = "index.jsp";
        return;
    }

    second.innerHTML = svalue;
}

/**
 * 1.获取XMLHttpRequest对象
 */

function getXMLHttpRequest() {
    var xmlhttp;
    if (window.XMLHttpRequest) {// code for all new browsers
        xmlhttp = new XMLHttpRequest();
    } else if (window.ActiveXObject) {// code for IE5 and IE6
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    return xmlhttp;

}

/**
 2.文件上传
@Param fileinput_upload 上传file文件id  string
@Param fileForm 上传file文件表单id    string
@Param hidden_img hidden上传图片到数据库id  string
@Param show_img 回显图片 id     string
 */
function onfileUpLoad(fileinput_upload,fileForm,hidden_img,show_img){
    //form,showphoto,photo
    if(document.getElementById(fileinput_upload).value==""){
        //什么也没选，就什么也不做
        return ;
    }
    var fileForm=document.getElementById(fileForm);

    //用ajax把图片上传到服务器
    var xmlhttp=getXMLHttpRequest();
    xmlhttp.onreadystatechange=function(){
        if(xmlhttp.readyState==4&&xmlhttp.status==200){
            var imgPath=xmlhttp.responseText;
            console.log("回来的图片sec路径:"+imgPath)
            if("error"==imgPath){
                imgPath="images/error.png";
            }
            document.getElementById(hidden_img).value=imgPath;
            document.getElementById(show_img).src=imgPath;
        }
    };
    xmlhttp.open("post",fileForm.action);
    xmlhttp.send(new FormData(fileForm));
}

/**
  3-1.验证码输入框验证
 @Param imageCode 验证码输入框id 如: "imageCode"
 @Param Codefont 验证码font提示 id 如: "Codefont"
 @Param url 提交url 如: "getcodeword"
 */
function onCodekeyup(imageCode,Codefont,url) {
    var imageCode = document.getElementById(imageCode);
    var codefont = document.getElementById(Codefont);
    var bool=false;
    if (imageCode.value.length == 4) {
        // var url = "getcodeword";
        var xmlhttp = getXMLHttpRequest();
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                var imgPath = xmlhttp.responseText;
                if (imageCode.value==imgPath){

                    codefont.setAttribute("color","greed");
                    codefont.innerHTML="验证码正确";
                    bool= true;
                }else{
                    codefont.setAttribute("color","red");
                    codefont.innerHTML="验证码错误";
                    bool= false;
                }

            }
        }
        xmlhttp.open("get",url)
        xmlhttp.send()
    }else{
        codefont.setAttribute("color","red");
        codefont.innerHTML="验证码错误";
        bool= false;
    }
    return bool;
}

/**
  3-2.点击替换验证码
 @Param imgid 点击图片/按钮 id (切换验证码) 如: "img"

 @Param src_url 提交url前部分 如: "${pageContext.request.contextPath}/imageCode?time="
 */
function changeImage(imgid,src_url) {

    document.getElementById(imgid).src =src_url+ new Date().getTime();
    onCodekeyup();
}