// 객체 초기화
     var IMP = window.IMP;
     IMP.init("imp43843243");

     function requestPay() {
       // 결제창 호출      
       IMP.request_pay({
           // 파라미터 값 설정 
           pg: "kakaopay.TC0ONETIME",
           pay_method: "card",
           merchant_uid: "57008833-33004", // 상점 고유 주문번호
           name: "옵션A",
           amount: 5000,
           buyer_email: "good@portone.io",
           buyer_name: "포트원 기술지원팀",
           buyer_tel: "010-1234-5678",
           buyer_addr: "서울특별시 강남구 삼성동",
           buyer_postcode: "123-456",
         },
         function (rsp) {         	
           // callback
           //rsp.imp_uid 값으로 결제 단건조회 API를 호출하여 결제결과를 판단합니다.
         }
       );
   }
     