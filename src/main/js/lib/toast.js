
let toastElement = null;
export function displayToast(message, duration=500){
  if(toastElement !== null){
    document.body.removeChild(toastElement);
  }
  toastElement = document.createElement('span');
  toastElement.setAttribute('class', 'toast');
  toastElement.innerHTML = message;
  document.body.appendChild(toastElement);
  setTimeout(function(){
    document.body.removeChild(toastElement);

  }, duration);
}