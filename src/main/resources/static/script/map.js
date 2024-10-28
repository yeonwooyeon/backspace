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
    const popupContent = `
        <h5>${place.info_name}</h5>
        <p>주소: ${place.info_add}</p>
        <p>건물유형: ${place.info_option}</p>
        <p>지불방식: ${place.info_type}</p>
        <p>면적: ${place.info_size}평</p>
        <p>권리금: ${place.option_money}만원</p>
        <p>보증금: ${place.info_deposit}만원</p>
        <p>관리비: ${place.option_cost}만원</p>
        <p>방갯수: ${place.info_count}개</p>
        <p>해당층수: ${place.info_fl}층</p>
        <p>전체층수: ${place.info_allfl}층</p>
        <p>호실: ${place.room_num}호</p>			
        <p>준공일: ${place.info_comp}</p>
        <p>입주가능일: ${place.info_move}</p>
        <p>상세옵션: ${place.option_op}</p>
        <p>기타옵션: ${place.option_etc}</p>
    `;
    
    document.getElementById('popupContent').innerHTML = popupContent;
    document.getElementById('popupModal').style.display = 'block'; // 모달 보여주기
}
document.getElementById('closePopupBtn').addEventListener('click', () => {
    document.getElementById('popupModal').style.display = 'none'; // 모달 숨기기
});

//건물DB
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
	        rentInfo = `${place.info_sell}`;
	    }
	    itemEl.innerHTML = `<h5>${place.info_name}</h5>
	                        <p>${place.info_add}</p>
	                        <p>건물 유형: ${place.info_option}</p>
	                        <p>${place.info_type}: ${rentInfo}/보증금: ${place.info_deposit}</p>`;	    
	    itemEl.addEventListener('click', function() {	 
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
	            } else {
	                alert('주소 검색에 실패했습니다.');
	            }
	        });
	    });
	    fragment.appendChild(itemEl);
	});	
	listEl.appendChild(fragment);
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