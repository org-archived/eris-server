/**
 * 
 */

let api = 'bv2av/api';

let bvel;
let avel;

window.addEventListener('load', function() {
	bvel = document.getElementById('bv');
	avel = document.getElementById('av');
}, false);

async function bv2av() {
	let data = await postData(api, {'bv': bvel.value});
	if (data.code) {
		bvel.value = data.message;
	} else {
		avel.value = data.data && data.data.av;
	}
}

async function av2bv() {
	let data = await postData(api, {'av': avel.value});
	if (data.code) {
		avel.value = data.message;
	} else {
		bvel.value = data.data && data.data.bv;
	}
}

async function postData(url, data) {
  return fetch(url, {
    body: JSON.stringify(data),
    cache: 'no-cache',
    credentials: 'same-origin',
    headers: {
      'content-type': 'application/json'
    },
    method: 'POST',
    mode: 'cors',
    redirect: 'follow',
    referrer: 'no-referrer',
  })
  .then(response => response.json())
}

