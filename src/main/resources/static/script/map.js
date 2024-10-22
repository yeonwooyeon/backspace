var map, marker, infowindow, geocoder;
	        var roadview = new kakao.maps.Roadview(document.getElementById('roadview'));
	        var roadviewClient = new kakao.maps.RoadviewClient();
	        var markers = [];
	
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
	                }
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
	            searchAddress();  // 주소 검색
	            searchDatabase(); // 데이터베이스 검색
	        }
	
	        function searchDatabase() {
	            const keyword = document.getElementById('address').value.trim();
				const type = document.getElementById('type').value; // 거래 유형 가져오기
	            if (!keyword) return alert('검색어를 입력하세요.');
	            fetch(`/search?keyword=${keyword}`)
	                .then(response => response.json())
	                .then(data => {
						allPlaces = data; // 전체 데이터 저장
						displaySearchResults(allPlaces); // 결과 표시
						
						console.log('Fetched data:', data); // 데이터 확인
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
					${place.image ? `<img src="${place.image}" alt="${place.info_name}" class="place-image" />` : ''}
	                    <p class="info_name">${place.info_name}</p>
	                    <p class="info_add">${place.info_add}</p>
	                    <p class="info_option">건물 유형: ${place.info_option}</p>
						<p class="info_type">거래 형태: ${place.info_type}</p>
						${place.info_year ? `<p class="info_year">전세값: ${place.info_year}</p>` : ''}
						${place.info_month ? `<p class="info_month">월세값: ${place.info_month}</p>` : ''}
						${place.info_sell ? `<p class="info_sell">매매값: ${place.info_sell}</p>` : ''}
						
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
	                            infowindow.setContent(`<div class="map_info_name">${place.info_name}</div>`);
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
	
			// 체크박스 필터링
			function filterResults() {
			    const filteredPlaces = allPlaces.filter(place => {
			        const infoOption = document.querySelector('input[name="info_option"]:checked');
			        const transactionType = document.querySelector('input[name="transaction_type"]:checked');
			        // 여기에 추가 필터링 로직을 넣을 수 있습니다.
			        return (infoOption ? place.info_option === infoOption.value : true) &&
			               (transactionType ? place.transaction_type === transactionType.value : true);
			    });
			    displaySearchResults(filteredPlaces);
			}
			
	        document.addEventListener('DOMContentLoaded', () => {
	            getLocation((lat, lng) => {
	                initializeMap(lat, lng);
	                displayRoadview(lat, lng);
	            });
	
	            document.getElementById('logo').addEventListener('click', () => window.location.href = '/index');
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
	            document.getElementById('locateMeBtn').addEventListener('click', () => {
	                getLocation((lat, lng) => {
	                    const coords = new kakao.maps.LatLng(lat, lng);
	                    map.setLevel(3);
	                    map.setCenter(coords);
	                    marker.setPosition(coords);
	                    displayRoadview(lat, lng);
	                });
	            });
				
				// 체크박스 변경 시 필터링 적용
				document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
				    checkbox.addEventListener('change', filterResults);
				});
	
	            // 데이터베이스에서 주소 목록을 불러와서 표시
	            fetch('/addresses')
	                .then(response => response.json())
	                .then(data => {
						allPlaces = data; // 전체 데이터 저장
	                    displaySearchResults(data);
	                });
	        });