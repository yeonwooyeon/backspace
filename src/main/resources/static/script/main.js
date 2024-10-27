function showLoginPopup() {
	document.getElementById("로그인팝업").style.display = "block";
}

function closeLoginPopup() {
	document.getElementById("로그인팝업").style.display = "none";
}

window.onclick = function(event) {
	var loginmodal = document.getElementById("로그인팝업");
	if (event.target == loginmodal) {
		loginmodal.style.display = "none";
	}
}

function countUp(elementId, target) {
	let countBox = document.querySelector(elementId);
	let count = 0;
	let duration = 1500;
	let intervalTime = 10;
	let steps = duration / intervalTime;
	let increment = target / steps;

	let counting = setInterval(function() {
		if (count >= target) {
			count = target;
			clearInterval(counting);
		} else {
			count += increment;
		}
		countBox.innerHTML = new Intl.NumberFormat().format(Math
			.floor(count))
			+ "건";
	}, intervalTime);
}

countUp('#공실정보', 312);
countUp('#임대정보', 124);
countUp('#신규공실정보', 32);