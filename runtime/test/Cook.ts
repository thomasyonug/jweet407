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
var Desk = new Proxy(__Desk, self.proxyHandler);
console.log('Cooker start!');
Desk.food_flag = 999;
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
var Desk = new Proxy(__Desk, self.proxyHandler);
console.log('Customer start!');
setTimeout(() => {
    console.log('Desk.food_flag:', Desk.food_flag);
}, 1000)
`;
}
const cooker = new Cook(); // Assuming Cook is a subclass of WebWorker
const customer = new Customer(); // Assuming Cook is a subclass of WebWorker
cooker.init();
customer.init();
cooker.start();
customer.start();
