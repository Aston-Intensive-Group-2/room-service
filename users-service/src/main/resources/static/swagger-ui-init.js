// Файл: resources/static/swagger-ui-init.js
//window.onload = function() {
 //   const serverSelect = document.querySelector('select[aria-label="Server selection"]');//document.querySelector('select.opblock-control__select');
  //  if (serverSelect) {
  //      serverSelect.addEventListener('change', (e) => {
 //           const selectedUrl = e.target.value;
 //           if (selectedUrl.includes('8080')) {
//                window.location.href = 'http://localhost:8080/swagger-ui.html'; // Projects API
 //           } else if (selectedUrl.includes('8081')) {
 //               window.location.href = 'http://localhost:8081/swagger-ui.html'; // Users API
 //           }
//        });
//    }
//};

//window.onload = function() {
//    const serverSelect = document.querySelector('select.opblock-control__select');
//    if (serverSelect) {
//        console.log("Dropdown найден:", serverSelect);
//        serverSelect.addEventListener('change', (e) => {
//            const selectedUrl = e.target.value;
//            console.log("Выбран URL:", selectedUrl);

 //           if (selectedUrl.includes('8080')) {
 //               console.log("Перенаправляем на Projects API");
 //               window.location.href = 'http://localhost:8080/swagger-ui.html';
 //           } else if (selectedUrl.includes('8081')) {
 //               console.log("Перенаправляем на Users API");
//                window.location.href = 'http://localhost:8081/swagger-ui.html';
//            }
//        });
//    } else {
 //       console.error("Dropdown не найден!");
 //   }
//};

$(document).ready(function() {
    // Функция для обработки выбора сервера
    function setupServerSwitcher() {
        const serverDropdown = $('select.opblock-control__select, select[aria-label="Server selection"], select[data-testid="servers"]');

        if (serverDropdown.length) {
            serverDropdown.off('change').on('change', function() {
                const selectedUrl = $(this).val();
                console.log("Выбран URL:", selectedUrl);

                if (selectedUrl.includes('projects.server.url') || selectedUrl.includes('http://localhost:8080')) {
                    console.log("Перенаправляем на Projects API");
                    window.location.href = 'http://localhost:8080/swagger-ui.html';
                } else if (selectedUrl.includes('api.server.url') || selectedUrl.includes('http://localhost:8081')) {
                    console.log("Перенаправляем на Users API");
                    window.location.href = 'http://localhost:8081/swagger-ui.html';
                }
            });
            return true;
        }
        return false;
    }

    // Первая попытка установки обработчика
    if (!setupServerSwitcher()) {
        // Если элемент не найден сразу, ждем его появления
        const observer = new MutationObserver(function() {
            if (setupServerSwitcher()) {
                observer.disconnect();
            }
        });
        observer.observe(document.body, { childList: true, subtree: true });
    }
});