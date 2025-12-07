import { injectGlobalWebcomponentCss } from 'Frontend/generated/jar-resources/theme-util.js';

import '@vaadin/side-nav/src/vaadin-side-nav.js';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/app-layout/src/vaadin-app-layout.js';
import '@vaadin/tooltip/src/vaadin-tooltip.js';
import '@vaadin/icon/src/vaadin-icon.js';
import '@vaadin/side-nav/src/vaadin-side-nav-item.js';
import '@vaadin/button/src/vaadin-button.js';
import 'Frontend/generated/jar-resources/buttonFunctions.js';
import '@vaadin/icons/vaadin-iconset.js';
import '@vaadin/vertical-layout/src/vaadin-vertical-layout.js';
import '@vaadin/app-layout/src/vaadin-drawer-toggle.js';
import '@vaadin/horizontal-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';
import 'Frontend/generated/jar-resources/ReactRouterOutletElement.tsx';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === 'e3b2cfde282e38e8679bd2298bdf21a3144a2ac6fbf26f7f11aaeca5c23a22e5') {
    pending.push(import('./chunks/chunk-2b85016f1cabb717e4158dae3c8677ae41f9291654ee67baa2145b62651a1f1d.js'));
  }
  if (key === 'c2afdaad59ac3420016ee9bdc769c4e9d6024bc09837388a78fbaa0e40b048e7') {
    pending.push(import('./chunks/chunk-f1264fca5bd113a92af4eea962f408064afd7059043bcb8014b1596a94d388fd.js'));
  }
  if (key === '36da26e19068d24bf241f27e2d75f56b7e5ef252a35251a832a8a54879c3fe23') {
    pending.push(import('./chunks/chunk-9329caa51413c63450d45c5a112e834864e8e5920b4556f5a41daf89cce89f83.js'));
  }
  if (key === '5dc32627b02cd1a96ce2a7467928aa2eac9a2ee7e5fdb97fea6ac77ec493ff2a') {
    pending.push(import('./chunks/chunk-3f6117bfddf534b402cb049109c46d7d89b678990cce8853c6afe0b6cadbf362.js'));
  }
  if (key === 'dec899677c8290f01b0a72c208e4e345881326bbb016d12cabcf0d69b52b796d') {
    pending.push(import('./chunks/chunk-e369b8bc4f1fdda5f63e8c40c62fc795e8a4d6023e9d973b398646ad1c4ab031.js'));
  }
  if (key === 'aaa8d660b0f18ad8b35dd933b065a03760eaeed0f0f01cdb00df4827902bf685') {
    pending.push(import('./chunks/chunk-d92a06a7762515ac5eda73ee8e6b3de6dd7322906dce88c06ef0f1a15023e386.js'));
  }
  if (key === '963309e895e9c243c04e9ee97d84241f986dcb524d1fa958588a9860bb4f3f1d') {
    pending.push(import('./chunks/chunk-a347b7fbe8eb6a45394e8146e2259ffdbe33a7606d80df82df242f405470baa9.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}