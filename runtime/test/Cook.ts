import WebWorker from "../src/WebWorker";

export class Cook extends WebWorker {

    __captured_cvs = { 'Desk.food_flag': 'Desk.food_flag' ,'Desk.count': 'Desk.count'}
    // Class definition
    source =`
// Worker thread JavaScript code
class __Desk {
    static food_flag = 0;
    static count = 10;
    static className = 'Desk';
}
var Desk = new Proxy(__Desk, proxyHandler);
console.log('Cooker start!');
console.log('Desk.count:', Desk.count);
Desk.food_flag = 999;
setTimeout(() => {
    Desk.count = 20;
    console.log('Desk.count:', Desk.count);
}, 1000)

`;
}
export class Customer extends WebWorker {

    __captured_cvs = { 'Desk.food_flag': 'Desk.food_flag' ,'Desk.count': 'Desk.count' }
    // Class definition
    source =`
// Worker thread JavaScript code
class __Desk {
    static food_flag = 0;
    static count = 10;
    static className = 'Desk';
}
// self.__Desk = __Desk;
var Desk = new Proxy(__Desk, proxyHandler);
console.log('Customer start!');
setTimeout(() => {
    console.log('Desk.food_flag:', Desk.food_flag);
}, 1000)
`;
}
const cooker = new Cook(); // Assuming Cook is a subclass of WebWorker
const customer = new Customer(); // Assuming Cook is a subclass of WebWorker
cooker.start();
customer.start();