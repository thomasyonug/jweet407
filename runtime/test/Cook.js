const WebWorker = require('../src/WebWorker')

class Cook extends WebWorker {
    __captured_cvs = { 'Desk.food_flag': 'Desk.food_flag', 'Desk.count': 'Desk.count' };
    // Class definition
    source = `
// Worker thread JavaScript code
class __Desk {
    static food_flag = 0;
    static count = 10;
}
var Desk = buildProxy(__Desk);
console.log('Cooker start!');
Desk.food_flag = 999;
`;
}
class Customer extends WebWorker {
    __captured_cvs = { 'Desk.food_flag': 'Desk.food_flag', 'Desk.count': 'Desk.count' };
    // Class definition
    source = `
// Worker thread JavaScript code
class __Desk {
    static food_flag = 0;
    static count = 10;
}
// self.__Desk = __Desk;
var Desk = buildProxy(__Desk);
console.log('Customer start!');
setTimeout(() => {
    console.log('Desk.food_flag:', Desk.food_flag);
}, 1000)
`;
}
// class Customer2 extends WebWorker {
//     __captured_cvs = { 'Desk.food_flag': 'Desk.food_flag', 'Desk.count': 'Desk.count' };
//     // Class definition
//     source = `
// // Worker thread JavaScript code
// class __Desk {
//     static food_flag = 0;
//     static count = 10;
// }
// // self.__Desk = __Desk;
// var Desk = buildProxy(__Desk);
// console.log('Customer start!');
// setTimeout(() => {
//     console.log('Desk.food_flag:', Desk.food_flag);
// }, 1000)
// `;
// }
const cooker = new Cook(); 
const customer = new Customer(); 
// const customer2 = new Customer2(); 
cooker.init();
customer.init();
// customer2.init();
cooker.start();
customer.start();
// customer2.start();