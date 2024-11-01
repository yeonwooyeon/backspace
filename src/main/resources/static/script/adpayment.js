// 객체 초기화
var IMP = window.IMP;
IMP.init("imp43843243");

let selectedOption = {
    name: '',
    price: 0
};

//선택한옵션 표시
function selectOption(optionName, optionPrice) {
    selectedOption.name = optionName;
    selectedOption.price = optionPrice;       
    document.getElementById("selectedOption").innerHTML = `
        ✅ 옵션  ${optionName} (${optionPrice.toLocaleString()}원)  <br><br>
        결제를 원하시면 아래의 구독버튼을 눌러주세요.
    	`;    
    const selectedOptionDiv = document.getElementById("selectedOption");
    selectedOptionDiv.style.animation = "shake 0.5s infinite";    
    setTimeout(function() {
        selectedOptionDiv.style.animation = "none"; 
    }, 1000); // 1초 후에 멈춤
}

// 결제 요청 함수
function requestPay() {
    if (selectedOption.name === '') {
        alert('옵션을 선택해 주세요.'); // 옵션이 선택되지 않았을 경우 경고
        return;
    }

    // 결제창 호출      
    IMP.request_pay({
        // 파라미터 값 설정 
        pg: "kakaopay.TC0ONETIME",
        pay_method: "card",
        merchant_uid: "57008833-33004", 
		name: "옵션 " + selectedOption.name,
        amount: selectedOption.price, 
        buyer_email: "good@portone.io",
        buyer_name: "포트원 기술지원팀",
        buyer_tel: "010-1234-5678",
        buyer_addr: "서울특별시 강남구 삼성동",
        buyer_postcode: "123-456",
    },		 
    function (rsp) {         	
        // callback
        // rsp.imp_uid 값으로 결제 단건조회 API를 호출하여 결제 결과를 판단합니다.
        if (rsp.success) {
            alert('결제가 완료되었습니다. 결제 ID : ' + rsp.imp_uid);
        } else {
            alert('결제에 실패했습니다.\n' + rsp.error_msg + '.');
        }
    });
}