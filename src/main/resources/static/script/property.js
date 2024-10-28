document.addEventListener("DOMContentLoaded", function() {
	// 상세보기 버튼 클릭 이벤트
	document.querySelectorAll('.detail-button').forEach(button => {
		button.addEventListener('click', function(event) {
			event.preventDefault(); // 기본 링크 동작 방지
			const infoNo = this.getAttribute('data-info-no');

			// AJAX 요청
			fetch(`/property/view?info_no=${infoNo}`)
				.then(response => response.json())
				.then(data => {
					// 팝업 내용 업데이트
					document.getElementById("property-name").innerText = data.info_name;
					document.getElementById("property-type").innerText = data.info_type;
					document.getElementById("property-month").innerText = data.info_month;
					document.getElementById("property-year").innerText = data.info_year;
					document.getElementById("property-sell").innerText = data.info_sell;
					document.getElementById("property-add").innerText = data.info_add;
					document.getElementById("property-status").innerText = data.status;
					document.getElementById("property-allfl").innerText = data.info_allfl;
					document.getElementById("property-fl").innerText = data.info_fl;
					document.getElementById("property-roomnum").innerText = data.room_num;
					document.getElementById("property_optionmoney").innerText = data.option_money;
					document.getElementById("property-deposit").innerText = data.info_deposit;
					document.getElementById("property-optioncost").innerText = data.option_cost;
					document.getElementById("property-size").innerText = data.info_size;
					document.getElementById("property-count").innerText = data.info_count;
					document.getElementById("property-comp").innerText = formatDate(data.info_comp);
					document.getElementById("property-move").innerText = formatDate(data.info_move);
					document.getElementById("property-optionop").innerText = data.option_op;
					document.getElementById("property-optionetc").innerText = data.option_etc;


					//이미지 업데이트
					const imageContainer = document.getElementById("property-image-container");
					imageContainer.innerHTML = ""; // 기존 이미지를 초기화

					data.images.forEach(image => {
						const imgElement = document.createElement("img");
						imgElement.src = image.si_insideurl; // 이미지 URL 설정
						imgElement.alt = "Property Image";
						imgElement.style.maxWidth = "100%"; // 스타일 조정
						imgElement.style.height = "auto";
						imageContainer.appendChild(imgElement); // 이미지 추가
					});

					// 팝업 보여주기
					document.getElementById("popup").style.display = "block";
				})
				.catch(error => console.error('Error:', error));

			function formatDate(dateString) {
				const date = new Date(dateString);
				const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
				return date.toLocaleDateString('ko-KR', options);
			}
		});
	});

	// 팝업 닫기
	const closeButton = document.querySelector(".close-button");
	closeButton.addEventListener("click", function() {
		document.getElementById("popup").style.display = "none"; // 팝업 닫기
	});

	// 팝업창 외부 클릭 시 닫기
	window.addEventListener("click", function(event) {
		const popup = document.getElementById("popup");
		if (event.target === popup) {
			popup.style.display = "none";
		}
	});
});