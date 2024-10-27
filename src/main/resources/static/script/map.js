var map, marker, infowindow, geocoder;
var roadview = new kakao.maps.Roadview(document.getElementById('roadview'));
var roadviewClient = new kakao.maps.RoadviewClient();
var markers = [];
var allPlaces = [];

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

function performSearch() {
	searchAddress();
	searchDatabase();
}

function searchDatabase() {
	const keyword = document.getElementById('address').value.trim();
	if (!keyword) return alert('검색어를 입력하세요.');

	fetch(`/search?keyword=${keyword}`)
		.then(response => response.json())
		.then(data => {
			allPlaces = data; // 전체 데이터 저장
			displaySearchResults(allPlaces); // 결과 표시
		})
		.catch(error => console.error('Error fetching data:', error));
}

function displaySearchResults(places) {
	const listEl = document.getElementById('placesList');
	listEl.innerHTML = '';
	const fragment = document.createDocumentFragment();

	markers.forEach(marker => marker.setMap(null));
	markers = [];

	places.forEach((place) => {
		const itemEl = document.createElement('li');
		itemEl.innerHTML = `
                    <h5>${place.info_name}</h5>
                    <p>${place.info_add}</p>
                    <p>건물 유형: ${place.info_option}</p>
                `;
		// 클릭 시 주소로 위치 이동
		itemEl.addEventListener('click', function() {
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
				} else {
					alert('주소 검색에 실패했습니다.');
				}
			});
		});
		fragment.appendChild(itemEl);
	});
	listEl.appendChild(fragment);
}

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
			if (op === "풀옵") return fullOptions.every(opt => place.option_op && place.option_op.includes(opt)); // 모든 풀옵 조건 포함
			if (op === "무옵") return !place.option_op; // 옵션이 없을 때 무옵으로 처리
		});

		const matchesOptionEtc = optionEtcs.length === 0 || optionEtcs.every(etc => place.option_etc && place.option_etc.includes(etc));

		return matchesInfoOption && matchesInfoType && matchesInfoCount && matchesInfoSize && matchesInfoMonth &&
			matchesInfoDeposit && matchesInfoFloor && matchesInfoComp && matchesOptionOp && matchesOptionEtc;
	});

	// option_etc 필터링 조건을 처리하여 각 체크박스의 조합에 따라 필터링
	const filteredByEtc = optionEtcs.length === 0 ? filteredPlaces : filteredPlaces.filter(place => {
		return optionEtcs.every(etc => place.option_etc && place.option_etc.includes(etc));
	});

	displaySearchResults(filteredByEtc);
}

// 체크박스 변경 시 필터링 적용
document.querySelectorAll('input[name="info_option"], input[name="info_count"], input[name="info_type"], input[name="info_size"], input[name="info_month"], input[name="info_deposit"], input[name="info_fl"], input[name="info_comp"], input[name="option_op"], input[name="option_etc"]').forEach(checkbox => {
	checkbox.addEventListener('change', filterResults);
});

//..
document.addEventListener('DOMContentLoaded', () => {
	// 내 위치 버튼 설정
	document.getElementById('locateMeBtn').addEventListener('click', () => {
		getLocation((lat, lng) => {
			const coords = new kakao.maps.LatLng(lat, lng);
			map.setLevel(3);
			map.setCenter(coords);
			marker.setPosition(coords);
			displayRoadview(lat, lng);
		});
	});

	// 검색 버튼 설정
	document.getElementById('addressSearchBtn').addEventListener('click', performSearch);

	// 리셋 버튼 설정
	document.getElementById('resetBtn').addEventListener('click', () => {
		document.getElementById('address').value = '';
		document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => checkbox.checked = false);
		displaySearchResults(allPlaces); // 모든 결과 다시 표시
	});

	// Enter 키로 검색 실행
	document.getElementById('address').addEventListener('keydown', (e) => {
		if (e.key === 'Enter') {
			e.preventDefault();
			performSearch();
		}
	});

	// 초기 위치 설정
	getLocation((lat, lng) => {
		initializeMap(lat, lng);
		displayRoadview(lat, lng);
	});

	// 데이터베이스에서 주소 목록 불러오기
	fetch('/addresses')
		.then(response => response.json())
		.then(data => {
			allPlaces = data; // 전체 데이터 저장
			displaySearchResults(allPlaces); // 결과 표시
		})
		.catch(error => console.error('Error fetching addresses:', error));
});