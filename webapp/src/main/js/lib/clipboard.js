export function copyToClipboard(text){
  const virtualComponent = document.createElement('pre');
  virtualComponent.innerHTML = text;
  document.body.appendChild(virtualComponent);
  selectElementText(virtualComponent);
  document.execCommand('copy');
  document.body.removeChild(virtualComponent);
}

export function selectElementText(element){ //http://www.javascriptkit.com/javatutors/copytoclipboard.shtml
  var range = document.createRange(); // create new range object
  range.selectNodeContents(element); // set range to encompass desired element text
  var selection = window.getSelection(); // get Selection object from currently user selected text
  selection.removeAllRanges(); // unselect any user selected text (if any)
  selection.addRange(range); // add range to Selection object to select it
}