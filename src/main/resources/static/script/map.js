var map, marker, infowindow, geocoder;
var roadview = new kakao.maps.Roadview(document.getElementById('roadview'));
var roadviewClient = new kakao.maps.RoadviewClient();
var markers = [];
var allPlaces = [];
//지도
function initializeMap(lat, lng) {
	map = new kakao.maps.Map(document.getElementById('map'), {
		center: new kakao.maps.LatLng(lat, lng),
		level: 3
	});
	map.addControl(new kakao.maps.MapTypeControl(), kakao.maps.ControlPosition.TOPRIGHT);
	map.addControl(new kakao.maps.ZoomControl(), kakao.maps.ControlPosition.RIGHT);
	marker = new kakao.maps.Marker({ position: new kakao.maps.LatLng(lat, lng), map: map });
	infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });
	geocoder = new kakao.maps.services.Geocoder();
	kakao.maps.event.addListener(map, 'click', (e) => {
		map.setLevel(3);
		marker.setPosition(e.latLng);
		displayRoadview(e.latLng.getLat(), e.latLng.getLng());
	});
}
//로드뷰
function displayRoadview(lat, lng) {
	var position = new kakao.maps.LatLng(lat, lng);
	roadviewClient.getNearestPanoId(position, 50, (panoId) => {
		if (panoId) {
			roadview.setPanoId(panoId, position);
		} //else {
		//alert('로드뷰 지원을 하지 않는 장소입니다.');
		//}
	});
}
//위치
function getLocation(callback) {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(
			position => callback(position.coords.latitude, position.coords.longitude),
			error => alert('위치 정보를 사용할 수 없습니다.')
		);
	} else {
		alert('현재 위치를 지원하지 않는 브라우저입니다.');
	}
}
//검색
function performSearch() {
	searchAddress();
	searchDatabase();
}

function searchAddress() {
	const address = document.getElementById('address').value.trim();
	if (!address) return alert('주소를 입력해주세요!');
	geocoder.addressSearch(address, (result, status) => {
		if (status === kakao.maps.services.Status.OK) {
			const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
			map.setLevel(3);
			map.setCenter(coords);
			marker.setPosition(coords);
			displayRoadview(result[0].y, result[0].x);
		} else {
			alert('검색어를 확인해주세요.');
		}
	});
}

function searchDatabase() {
	const keyword = document.getElementById('address').value.trim();
	if (!keyword) return alert('검색어를 입력하세요.');
	fetch(`/search?keyword=${keyword}`)
		.then(response => response.json())
		.then(data => {
			allPlaces = data;
			displaySearchResults(allPlaces);
		})
		.catch(error => console.error('Error fetching data:', error));
}

function showPopup(place) {
	// 각 요소의 내용을 설정
	document.getElementById('property-name').textContent = place.info_name || 'N/A';
	document.getElementById('property-add').textContent = place.info_add || 'N/A';
	document.getElementById('property-type').textContent = place.info_option || 'N/A';

	// 가격 관련 요소 설정
	document.getElementById('property-month').textContent = place.info_month || '';
	document.getElementById('property-year').textContent = place.info_year || '';
	document.getElementById('property-sell').textContent = place.info_type || ''; // 필요에 따라 수정

	document.getElementById('property_optionmoney').textContent = place.option_money || '0';
	document.getElementById('property-deposit').textContent = place.info_deposit || '0';
	document.getElementById('property-optioncost').textContent = place.option_cost || '0';
	document.getElementById('property-size').textContent = place.info_size || '0';
	document.getElementById('property-count').textContent = place.info_count || '0';
	document.getElementById('property-fl').textContent = place.info_fl || '0';
	document.getElementById('property-allfl').textContent = place.info_allfl || '0';
	document.getElementById('property-roomnum').textContent = place.room_num || '0';
	document.getElementById('property-comp').textContent = place.info_comp || 'N/A';
	document.getElementById('property-move').textContent = place.info_move || 'N/A';
	document.getElementById('property-optionop').textContent = place.option_op || 'N/A';
	document.getElementById('property-optionetc').textContent = place.option_etc || 'N/A';

	// 주소 및 상태 등 추가 정보 설정
	document.getElementById('property-status').textContent = place.info_status || 'N/A'; // 예시 추가
	// 필요한 경우 다른 필드도 추가로 설정

	// 이미지 컨테이너 처리 (옵션)
	// 이미지 컨테이너 처리
	const imageContainer = document.getElementById('property-image-container');
	imageContainer.innerHTML = ''; // 기존 이미지 제거

	if (place.images && Array.isArray(place.images)) {
		place.images.forEach(image => {
			let imgUrl = '';

			if (typeof image === 'string') {
				// 이미지가 문자열 URL인 경우
				imgUrl = image;
			} else if (image.si_insideurl) {
				// 이미지가 객체이고 si_insideurl 속성이 있는 경우
				imgUrl = image.si_insideurl;
			} else if (image.url) {
				// 이미지가 객체이고 url 속성이 있는 경우
				imgUrl = image.url;
			}

			if (imgUrl && imgUrl.trim() !== '') {
				const img = document.createElement('img');
				img.src = imgUrl;
				img.alt = place.info_name || 'Property Image';
				img.style.maxWidth = "100%"; // 스타일 조정
				img.style.height = "auto";
				img.style.marginBottom = "10px"; // 이미지 간 간격 추가

				// 이미지 로드 실패 시 기본 이미지로 대체
				img.onerror = function() {
					this.src = 'https://via.placeholder.com/400x200.png?text=No+Image';
				};

				imageContainer.appendChild(img);
			}
		});
	}

	// 팝업 보이기
	document.getElementById('popup').style.display = 'block';
}

// 팝업 닫기 버튼 이벤트 리스너 설정
document.querySelector('#popup .close-button').addEventListener('click', () => {
	document.getElementById('popup').style.display = 'none';
});


//건물DB
let cart = [];  // 장바구니 배열 (전역에서 관리)

function displaySearchResults(places) {
	const listEl = document.getElementById('placesList');
	listEl.innerHTML = '';
	const fragment = document.createDocumentFragment();
	markers.forEach(marker => marker.setMap(null));
	markers = [];
	places.forEach((place) => {
		const itemEl = document.createElement('li');
		let rentInfo = '';
		if (place.info_type === "월세") {
			rentInfo = `${place.info_month}`;
		} else if (place.info_type === "전세") {
			rentInfo = `${place.info_year}`;
		} else if (place.info_type === "매매") {
			rentInfo = `${place.info_sell}`; // 매매의 경우에 대한 처리를 추가하세요
		}
		// 매물의 이미지를 위한 이미지 요소 생성
		if (place.images && place.images.length > 0) {
			const imageEl = document.createElement('img');
			imageEl.src = place.images && place.images.length > 0 ? place.images[0].si_insideurl : 'default-image-url.jpg'; // 기본 이미지 URL 설정
			imageEl.alt = place.info_name; // 이미지에 대한 설명
			itemEl.appendChild(imageEl); // 이미지 요소를 가장 위에 추가
		}

		itemEl.innerHTML += `
							<h5>${place.info_name}</h5>
                   			<p>${place.info_add}</p>
                    		<p>건물 유형: ${place.info_option}</p>							
                    		<p>${place.info_type}: ${rentInfo}/보증금: ${place.info_deposit}</p>							
							`;

		// 장바구니 담기 버튼
		const cartButton = document.createElement('button');
		cartButton.innerText = '담기';
		cartButton.classList.add('cart-btn');
		cartButton.addEventListener('click', function(event) {
			event.stopPropagation();  // 클릭 이벤트가 부모 요소로 전파되지 않도록 방지
			addToCart(place);         // 장바구니에 추가하는 함수 호출
		});

		// 장바구니 버튼을 항목에 추가
		itemEl.appendChild(cartButton);

		// 매물 클릭 시 지도에서 위치 표시 및 팝업 열기
		itemEl.addEventListener('click', function() {
			event.stopPropagation();
			showPopup(place); // 모달 열기       
			const address = place.info_add;
			geocoder.addressSearch(address, (result, status) => {
				if (status === kakao.maps.services.Status.OK) {
					const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
					map.setLevel(3);
					map.setCenter(coords);
					const marker = new kakao.maps.Marker({
						map: map,
						position: coords
					});
					infowindow.setContent(`<div style="padding:5px;">${place.info_name}</div>`);
					infowindow.open(map, marker);
				}
			});
		});
		fragment.appendChild(itemEl);
	});
	listEl.appendChild(fragment);
}


// 장바구니에 매물 추가 함수 (AJAX로 서버에 전송)
function addToCart(place) {
	
	const propertyId = place.info_no; 
	const userId = place.id;
	
	// 콘솔로 보내는 데이터 확인
	 console.log("Sending data to server:", { propertyId, userId });
	 
	// 서버로 POST 요청을 보냄
	fetch('/add-to-cart', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			propertyId: propertyId,
			userId: userId
		})
	})
	.then(response => response.json())
	.then(data => {
		if (data.success) {
			alert(`${place.info_name}이 장바구니에 추가되었습니다.`);
		} else {
			alert('장바구니 추가 실패');
		}
	})
	.catch(error => {
		alert('서버 오류가 발생했습니다. 다시 시도해주세요.');
		console.error(error);
	});
}

//필터링
function filterResults() {
	const infoOptions = Array.from(document.querySelectorAll('input[name="info_option"]:checked')).map(checkbox => checkbox.value);
	const infoCounts = Array.from(document.querySelectorAll('input[name="info_count"]:checked')).map(checkbox => parseInt(checkbox.value, 10));
	const infoTypes = Array.from(document.querySelectorAll('input[name="info_type"]:checked')).map(checkbox => checkbox.value);
	const infoSizes = Array.from(document.querySelectorAll('input[name="info_size"]:checked')).map(checkbox => parseInt(checkbox.value, 10));
	const infoMonths = Array.from(document.querySelectorAll('input[name="info_month"]:checked')).map(checkbox => parseInt(checkbox.value, 10));
	const infoDeposits = Array.from(document.querySelectorAll('input[name="info_deposit"]:checked')).map(checkbox => parseInt(checkbox.value, 10));
	const infoFloors = Array.from(document.querySelectorAll('input[name="info_fl"]:checked')).map(checkbox => checkbox.value);
	const infoComps = Array.from(document.querySelectorAll('input[name="info_comp"]:checked')).map(checkbox => parseInt(checkbox.value, 10));
	const optionOps = Array.from(document.querySelectorAll('input[name="option_op"]:checked')).map(checkbox => checkbox.value);
	const optionEtcs = Array.from(document.querySelectorAll('input[name="option_etc"]:checked')).map(checkbox => checkbox.value);
	const fullOptions = ['세탁기', '에어컨', '인덕션', '가스렌지', 'TV', '침대']; // 풀옵 조건에 필요한 옵션들
	const filteredPlaces = allPlaces.filter(place => {
		const matchesInfoOption = infoOptions.length === 0 || infoOptions.includes(place.info_option);
		const matchesInfoType = infoTypes.length === 0 || infoTypes.includes(place.info_type);
		const matchesInfoCount = infoCounts.length === 0 || infoCounts.includes(place.info_count);
		const matchesInfoSize = infoSizes.length === 0 || infoSizes.some(size => {
			if (size === 9) return place.info_size < 10;
			if (size === 10) return place.info_size >= 10 && place.info_size < 20;
			if (size === 20) return place.info_size >= 20 && place.info_size < 30;
			if (size === 30) return place.info_size >= 30 && place.info_size < 40;
			if (size === 40) return place.info_size >= 40;
		});
		const matchesInfoMonth = infoMonths.length === 0 || (place.info_month !== null && infoMonths.some(month => {
			if (month === 9) return place.info_month < 10;
			if (month === 10) return place.info_month >= 10 && place.info_month < 30;
			if (month === 30) return place.info_month >= 30 && place.info_month < 50;
			if (month === 50) return place.info_month >= 50;
		}));
		const matchesInfoDeposit = infoDeposits.length === 0 || (place.info_deposit !== null && infoDeposits.some(deposit => {
			if (deposit === 0) return place.info_deposit < 1000;
			if (deposit === 1000) return place.info_deposit >= 1000 && place.info_deposit < 4000;
			if (deposit === 4000) return place.info_deposit >= 4000 && place.info_deposit < 8000;
			if (deposit === 8000) return place.info_deposit >= 8000;
		}));
		const matchesInfoFloor = infoFloors.length === 0 || infoFloors.some(floor => {
			if (floor === "지하") return place.info_fl === "지하";
			if (floor === "1") return parseInt(place.info_fl, 10) === 1;
			if (floor === "2") return parseInt(place.info_fl, 10) === 2;
			if (floor === "3") return parseInt(place.info_fl, 10) >= 3;
			if (floor === "옥탑") return place.info_fl === "옥탑";
		});
		const currentYear = new Date().getFullYear();
		const buildingAge = currentYear - parseInt(place.info_comp, 10);
		const matchesInfoComp = infoComps.length === 0 || infoComps.some(comp => {
			if (comp === 0) return buildingAge <= 3;
			if (comp === 3) return buildingAge > 3 && buildingAge <= 10;
			if (comp === 10) return buildingAge > 10 && buildingAge <= 15;
			if (comp === 15) return buildingAge > 15;
		});
		const matchesOptionOp = optionOps.length === 0 || optionOps.some(op => {
			if (op === "풀옵") return fullOptions.every(opt => place.option_op && place.option_op.includes(opt));
			if (op === "무옵") return !place.option_op;
		});
		const matchesOptionEtc = optionEtcs.length === 0 || optionEtcs.every(etc => place.option_etc && place.option_etc.includes(etc));
		return matchesInfoOption && matchesInfoType && matchesInfoCount && matchesInfoSize && matchesInfoMonth &&
			matchesInfoDeposit && matchesInfoFloor && matchesInfoComp && matchesOptionOp && matchesOptionEtc;
	});
	const filteredByEtc = optionEtcs.length === 0 ? filteredPlaces : filteredPlaces.filter(place => {
		return optionEtcs.every(etc => place.option_etc && place.option_etc.includes(etc));
	});

	displaySearchResults(filteredByEtc);
}

document.querySelectorAll('input[name="info_option"], input[name="info_count"], input[name="info_type"], input[name="info_size"], input[name="info_month"], input[name="info_deposit"], input[name="info_fl"], input[name="info_comp"], input[name="option_op"], input[name="option_etc"]').forEach(checkbox => {
	checkbox.addEventListener('change', filterResults);
});

//클릭이벤트
document.addEventListener('DOMContentLoaded', () => {
	document.getElementById('locateMeBtn').addEventListener('click', () => {
		getLocation((lat, lng) => {
			const coords = new kakao.maps.LatLng(lat, lng);
			map.setLevel(3);
			map.setCenter(coords);
			marker.setPosition(coords);
			displayRoadview(lat, lng);
		});
	});

	document.getElementById('addressSearchBtn').addEventListener('click', performSearch);
	document.getElementById('resetBtn').addEventListener('click', () => {
		document.getElementById('address').value = '';
		document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => checkbox.checked = false);
		displaySearchResults(allPlaces); // 모든 결과 다시 표시
	});

	document.getElementById('address').addEventListener('keydown', (e) => {
		if (e.key === 'Enter') {
			e.preventDefault();
			performSearch();
		}
	});

	getLocation((lat, lng) => {
		initializeMap(lat, lng);
		displayRoadview(lat, lng);
	});

	fetch('/addresses')
		.then(response => response.json())
		.then(data => {
			allPlaces = data;
			displaySearchResults(allPlaces);
		})
		.catch(error => console.error('Error fetching addresses:', error));
});