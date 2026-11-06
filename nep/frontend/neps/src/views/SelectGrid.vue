<!-- src/views/SelectGrid.vue -->
<template>
  <div class="wrapper">
    <header>
      <img src="@/assets/header.png" alt="环保主题插图">
    </header>
    
    <h1>东软环保公众监督平台</h1>
    <p class="subtitle">选择举报区域</p>
    
    <section>
      <div class="form-item">
        <label>省份</label>
        <el-select 
          v-model="provinceId" 
          placeholder="请选择省份" 
          @change="handleProvinceChange"
          size="large"
          style="width: 100%"
        >
          <el-option
            v-for="item in provinceArr"
            :key="item.provinceId"
            :label="item.provinceName"
            :value="item.provinceId"
          />
        </el-select>
      </div>
      
      <div class="form-item">
        <label>城市</label>
        <el-select 
          v-model="cityId" 
          placeholder="请选择城市"
          size="large"
          style="width: 100%"
          :disabled="!provinceId"
        >
          <el-option
            v-for="item in cityArr"
            :key="item.cityId"
            :label="item.cityName"
            :value="item.cityId"
          />
        </el-select>
      </div>
      
      <div class="form-item">
        <label>详细地址</label>
        <el-input 
          v-model="address" 
          placeholder="请输入详细地址(街道、门牌号等)"
          size="large"
          clearable
        >
          <template #prefix>
            <el-icon><Location /></el-icon>
          </template>
        </el-input>
      </div>
      
      <div class="button-box" @click="handleSubmit">
        确 定
      </div>
      
      <div class="secondary-buttons">
        <div class="btn-secondary" @click="toFeedbackList">
          查看历史反馈
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, inject, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from 'element-plus';
import { Location } from '@element-plus/icons-vue';

const router = useRouter();
const axios = inject("axios");

const provinceArr = ref([]);
const cityArr = ref([]);
const provinceId = ref(null);
const cityId = ref(null);
const address = ref("");

// 真实的中国省份数据
const provincesData = [
  { id: 1, name: '北京市' },
  { id: 2, name: '天津市' },
  { id: 3, name: '河北省' },
  { id: 4, name: '山西省' },
  { id: 5, name: '内蒙古自治区' },
  { id: 6, name: '辽宁省' },
  { id: 7, name: '吉林省' },
  { id: 8, name: '黑龙江省' },
  { id: 9, name: '上海市' },
  { id: 10, name: '江苏省' },
  { id: 11, name: '浙江省' },
  { id: 12, name: '安徽省' },
  { id: 13, name: '福建省' },
  { id: 14, name: '江西省' },
  { id: 15, name: '山东省' },
  { id: 16, name: '河南省' },
  { id: 17, name: '湖北省' },
  { id: 18, name: '湖南省' },
  { id: 19, name: '广东省' },
  { id: 20, name: '广西壮族自治区' },
  { id: 21, name: '海南省' },
  { id: 22, name: '重庆市' },
  { id: 23, name: '四川省' },
  { id: 24, name: '贵州省' },
  { id: 25, name: '云南省' },
  { id: 26, name: '西藏自治区' },
  { id: 27, name: '陕西省' },
  { id: 28, name: '甘肃省' },
  { id: 29, name: '青海省' },
  { id: 30, name: '宁夏回族自治区' },
  { id: 31, name: '新疆维吾尔自治区' },
  { id: 32, name: '台湾省' },
  { id: 33, name: '香港特别行政区' },
  { id: 34, name: '澳门特别行政区' }
];

// 主要城市的映射关系
const citiesMap = {
  1: [{ id: 101, name: '北京市' }], // 北京
  2: [{ id: 201, name: '天津市' }], // 天津
  3: [ // 河北
    { id: 301, name: '石家庄市' },
    { id: 302, name: '唐山市' },
    { id: 303, name: '秦皇岛市' },
    { id: 304, name: '邯郸市' },
    { id: 305, name: '邢台市' },
    { id: 306, name: '保定市' },
    { id: 307, name: '张家口市' },
    { id: 308, name: '承德市' },
    { id: 309, name: '沧州市' },
    { id: 310, name: '廊坊市' },
    { id: 311, name: '衡水市' }
  ],
  4: [ // 山西
    { id: 401, name: '太原市' },
    { id: 402, name: '大同市' },
    { id: 403, name: '阳泉市' },
    { id: 404, name: '长治市' },
    { id: 405, name: '晋城市' },
    { id: 406, name: '朔州市' },
    { id: 407, name: '晋中市' },
    { id: 408, name: '运城市' },
    { id: 409, name: '忻州市' },
    { id: 410, name: '临汾市' },
    { id: 411, name: '吕梁市' }
  ],
  6: [ // 辽宁
    { id: 601, name: '沈阳市' },
    { id: 602, name: '大连市' },
    { id: 603, name: '鞍山市' },
    { id: 604, name: '抚顺市' },
    { id: 605, name: '本溪市' },
    { id: 606, name: '丹东市' },
    { id: 607, name: '锦州市' },
    { id: 608, name: '营口市' },
    { id: 609, name: '阜新市' },
    { id: 610, name: '辽阳市' },
    { id: 611, name: '盘锦市' },
    { id: 612, name: '铁岭市' },
    { id: 613, name: '朝阳市' },
    { id: 614, name: '葫芦岛市' }
  ],
  9: [{ id: 901, name: '上海市' }], // 上海
  10: [ // 江苏
    { id: 1001, name: '南京市' },
    { id: 1002, name: '无锡市' },
    { id: 1003, name: '徐州市' },
    { id: 1004, name: '常州市' },
    { id: 1005, name: '苏州市' },
    { id: 1006, name: '南通市' },
    { id: 1007, name: '连云港市' },
    { id: 1008, name: '淮安市' },
    { id: 1009, name: '盐城市' },
    { id: 1010, name: '扬州市' },
    { id: 1011, name: '镇江市' },
    { id: 1012, name: '泰州市' },
    { id: 1013, name: '宿迁市' }
  ],
  11: [ // 浙江
    { id: 1101, name: '杭州市' },
    { id: 1102, name: '宁波市' },
    { id: 1103, name: '温州市' },
    { id: 1104, name: '嘉兴市' },
    { id: 1105, name: '湖州市' },
    { id: 1106, name: '绍兴市' },
    { id: 1107, name: '金华市' },
    { id: 1108, name: '衢州市' },
    { id: 1109, name: '舟山市' },
    { id: 1110, name: '台州市' },
    { id: 1111, name: '丽水市' }
  ],
  15: [ // 山东
    { id: 1501, name: '济南市' },
    { id: 1502, name: '青岛市' },
    { id: 1503, name: '淄博市' },
    { id: 1504, name: '枣庄市' },
    { id: 1505, name: '东营市' },
    { id: 1506, name: '烟台市' },
    { id: 1507, name: '潍坊市' },
    { id: 1508, name: '济宁市' },
    { id: 1509, name: '泰安市' },
    { id: 1510, name: '威海市' },
    { id: 1511, name: '日照市' },
    { id: 1512, name: '临沂市' },
    { id: 1513, name: '德州市' },
    { id: 1514, name: '聊城市' },
    { id: 1515, name: '滨州市' },
    { id: 1516, name: '菏泽市' }
  ],
  16: [ // 河南
    { id: 1601, name: '郑州市' },
    { id: 1602, name: '开封市' },
    { id: 1603, name: '洛阳市' },
    { id: 1604, name: '平顶山市' },
    { id: 1605, name: '安阳市' },
    { id: 1606, name: '鹤壁市' },
    { id: 1607, name: '新乡市' },
    { id: 1608, name: '焦作市' },
    { id: 1609, name: '濮阳市' },
    { id: 1610, name: '许昌市' },
    { id: 1611, name: '漯河市' },
    { id: 1612, name: '三门峡市' },
    { id: 1613, name: '南阳市' },
    { id: 1614, name: '商丘市' },
    { id: 1615, name: '信阳市' },
    { id: 1616, name: '周口市' },
    { id: 1617, name: '驻马店市' }
  ],
  17: [ // 湖北
    { id: 1701, name: '武汉市' },
    { id: 1702, name: '黄石市' },
    { id: 1703, name: '十堰市' },
    { id: 1704, name: '宜昌市' },
    { id: 1705, name: '襄阳市' },
    { id: 1706, name: '鄂州市' },
    { id: 1707, name: '荆门市' },
    { id: 1708, name: '孝感市' },
    { id: 1709, name: '荆州市' },
    { id: 1710, name: '黄冈市' },
    { id: 1711, name: '咸宁市' },
    { id: 1712, name: '随州市' },
    { id: 1713, name: '恩施土家族苗族自治州' }
  ],
  19: [ // 广东
    { id: 1901, name: '广州市' },
    { id: 1902, name: '深圳市' },
    { id: 1903, name: '珠海市' },
    { id: 1904, name: '汕头市' },
    { id: 1905, name: '佛山市' },
    { id: 1906, name: '韶关市' },
    { id: 1907, name: '湛江市' },
    { id: 1908, name: '肇庆市' },
    { id: 1909, name: '江门市' },
    { id: 1910, name: '茂名市' },
    { id: 1911, name: '惠州市' },
    { id: 1912, name: '梅州市' },
    { id: 1913, name: '汕尾市' },
    { id: 1914, name: '河源市' },
    { id: 1915, name: '阳江市' },
    { id: 1916, name: '清远市' },
    { id: 1917, name: '东莞市' },
    { id: 1918, name: '中山市' },
    { id: 1919, name: '潮州市' },
    { id: 1920, name: '揭阳市' },
    { id: 1921, name: '云浮市' }
  ],
  22: [{ id: 2201, name: '重庆市' }], // 重庆
  23: [ // 四川
    { id: 2301, name: '成都市' },
    { id: 2302, name: '自贡市' },
    { id: 2303, name: '攀枝花市' },
    { id: 2304, name: '泸州市' },
    { id: 2305, name: '德阳市' },
    { id: 2306, name: '绵阳市' },
    { id: 2307, name: '广元市' },
    { id: 2308, name: '遂宁市' },
    { id: 2309, name: '内江市' },
    { id: 2310, name: '乐山市' },
    { id: 2311, name: '南充市' },
    { id: 2312, name: '眉山市' },
    { id: 2313, name: '宜宾市' },
    { id: 2314, name: '广安市' },
    { id: 2315, name: '达州市' },
    { id: 2316, name: '雅安市' },
    { id: 2317, name: '巴中市' },
    { id: 2318, name: '资阳市' },
    { id: 2319, name: '阿坝藏族羌族自治州' },
    { id: 2320, name: '甘孜藏族自治州' },
    { id: 2321, name: '凉山彝族自治州' }
  ],
  27: [ // 陕西
    { id: 2701, name: '西安市' },
    { id: 2702, name: '铜川市' },
    { id: 2703, name: '宝鸡市' },
    { id: 2704, name: '咸阳市' },
    { id: 2705, name: '渭南市' },
    { id: 2706, name: '延安市' },
    { id: 2707, name: '汉中市' },
    { id: 2708, name: '榆林市' },
    { id: 2709, name: '安康市' },
    { id: 2710, name: '商洛市' }
  ],
  5: [ // 内蒙古
    { id: 501, name: '呼和浩特市' },
    { id: 502, name: '包头市' },
    { id: 503, name: '乌海市' },
    { id: 504, name: '赤峰市' },
    { id: 505, name: '通辽市' },
    { id: 506, name: '鄂尔多斯市' },
    { id: 507, name: '呼伦贝尔市' },
    { id: 508, name: '巴彦淖尔市' },
    { id: 509, name: '乌兰察布市' },
    { id: 510, name: '兴安盟' },
    { id: 511, name: '锡林郭勒盟' },
    { id: 512, name: '阿拉善盟' }
  ],
  7: [ // 吉林
    { id: 701, name: '长春市' },
    { id: 702, name: '吉林市' },
    { id: 703, name: '四平市' },
    { id: 704, name: '辽源市' },
    { id: 705, name: '通化市' },
    { id: 706, name: '白山市' },
    { id: 707, name: '松原市' },
    { id: 708, name: '白城市' },
    { id: 709, name: '延边朝鲜族自治州' }
  ],
  8: [ // 黑龙江
    { id: 801, name: '哈尔滨市' },
    { id: 802, name: '齐齐哈尔市' },
    { id: 803, name: '鸡西市' },
    { id: 804, name: '鹤岗市' },
    { id: 805, name: '双鸭山市' },
    { id: 806, name: '大庆市' },
    { id: 807, name: '伊春市' },
    { id: 808, name: '佳木斯市' },
    { id: 809, name: '七台河市' },
    { id: 810, name: '牡丹江市' },
    { id: 811, name: '黑河市' },
    { id: 812, name: '绥化市' },
    { id: 813, name: '大兴安岭地区' }
  ],
  12: [ // 安徽
    { id: 1201, name: '合肥市' },
    { id: 1202, name: '芜湖市' },
    { id: 1203, name: '蚌埠市' },
    { id: 1204, name: '淮南市' },
    { id: 1205, name: '马鞍山市' },
    { id: 1206, name: '淮北市' },
    { id: 1207, name: '铜陵市' },
    { id: 1208, name: '安庆市' },
    { id: 1209, name: '黄山市' },
    { id: 1210, name: '滁州市' },
    { id: 1211, name: '阜阳市' },
    { id: 1212, name: '宿州市' },
    { id: 1213, name: '六安市' },
    { id: 1214, name: '亳州市' },
    { id: 1215, name: '池州市' },
    { id: 1216, name: '宣城市' }
  ],
  13: [ // 福建
    { id: 1301, name: '福州市' },
    { id: 1302, name: '厦门市' },
    { id: 1303, name: '莆田市' },
    { id: 1304, name: '三明市' },
    { id: 1305, name: '泉州市' },
    { id: 1306, name: '漳州市' },
    { id: 1307, name: '南平市' },
    { id: 1308, name: '龙岩市' },
    { id: 1309, name: '宁德市' }
  ],
  14: [ // 江西
    { id: 1401, name: '南昌市' },
    { id: 1402, name: '景德镇市' },
    { id: 1403, name: '萍乡市' },
    { id: 1404, name: '九江市' },
    { id: 1405, name: '新余市' },
    { id: 1406, name: '鹰潭市' },
    { id: 1407, name: '赣州市' },
    { id: 1408, name: '吉安市' },
    { id: 1409, name: '宜春市' },
    { id: 1410, name: '抚州市' },
    { id: 1411, name: '上饶市' }
  ],
  18: [ // 湖南
    { id: 1801, name: '长沙市' },
    { id: 1802, name: '株洲市' },
    { id: 1803, name: '湘潭市' },
    { id: 1804, name: '衡阳市' },
    { id: 1805, name: '邵阳市' },
    { id: 1806, name: '岳阳市' },
    { id: 1807, name: '常德市' },
    { id: 1808, name: '张家界市' },
    { id: 1809, name: '益阳市' },
    { id: 1810, name: '郴州市' },
    { id: 1811, name: '永州市' },
    { id: 1812, name: '怀化市' },
    { id: 1813, name: '娄底市' },
    { id: 1814, name: '湘西土家族苗族自治州' }
  ],
  20: [ // 广西
    { id: 2001, name: '南宁市' },
    { id: 2002, name: '柳州市' },
    { id: 2003, name: '桂林市' },
    { id: 2004, name: '梧州市' },
    { id: 2005, name: '北海市' },
    { id: 2006, name: '防城港市' },
    { id: 2007, name: '钦州市' },
    { id: 2008, name: '贵港市' },
    { id: 2009, name: '玉林市' },
    { id: 2010, name: '百色市' },
    { id: 2011, name: '贺州市' },
    { id: 2012, name: '河池市' },
    { id: 2013, name: '来宾市' },
    { id: 2014, name: '崇左市' }
  ],
  21: [ // 海南
    { id: 2101, name: '海口市' },
    { id: 2102, name: '三亚市' },
    { id: 2103, name: '三沙市' },
    { id: 2104, name: '儋州市' },
    { id: 2105, name: '五指山市' },
    { id: 2106, name: '琼海市' },
    { id: 2107, name: '文昌市' },
    { id: 2108, name: '万宁市' },
    { id: 2109, name: '东方市' }
  ],
  24: [ // 贵州
    { id: 2401, name: '贵阳市' },
    { id: 2402, name: '六盘水市' },
    { id: 2403, name: '遵义市' },
    { id: 2404, name: '安顺市' },
    { id: 2405, name: '毕节市' },
    { id: 2406, name: '铜仁市' },
    { id: 2407, name: '黔西南布依族苗族自治州' },
    { id: 2408, name: '黔东南苗族侗族自治州' },
    { id: 2409, name: '黔南布依族苗族自治州' }
  ],
  25: [ // 云南
    { id: 2501, name: '昆明市' },
    { id: 2502, name: '曲靖市' },
    { id: 2503, name: '玉溪市' },
    { id: 2504, name: '保山市' },
    { id: 2505, name: '昭通市' },
    { id: 2506, name: '丽江市' },
    { id: 2507, name: '普洱市' },
    { id: 2508, name: '临沧市' },
    { id: 2509, name: '楚雄彝族自治州' },
    { id: 2510, name: '红河哈尼族彝族自治州' },
    { id: 2511, name: '文山壮族苗族自治州' },
    { id: 2512, name: '西双版纳傣族自治州' },
    { id: 2513, name: '大理白族自治州' },
    { id: 2514, name: '德宏傣族景颇族自治州' },
    { id: 2515, name: '怒江傈僳族自治州' },
    { id: 2516, name: '迪庆藏族自治州' }
  ],
  26: [ // 西藏
    { id: 2601, name: '拉萨市' },
    { id: 2602, name: '日喀则市' },
    { id: 2603, name: '昌都市' },
    { id: 2604, name: '林芝市' },
    { id: 2605, name: '山南市' },
    { id: 2606, name: '那曲市' },
    { id: 2607, name: '阿里地区' }
  ],
  28: [ // 甘肃
    { id: 2801, name: '兰州市' },
    { id: 2802, name: '嘉峪关市' },
    { id: 2803, name: '金昌市' },
    { id: 2804, name: '白银市' },
    { id: 2805, name: '天水市' },
    { id: 2806, name: '武威市' },
    { id: 2807, name: '张掖市' },
    { id: 2808, name: '平凉市' },
    { id: 2809, name: '酒泉市' },
    { id: 2810, name: '庆阳市' },
    { id: 2811, name: '定西市' },
    { id: 2812, name: '陇南市' },
    { id: 2813, name: '临夏回族自治州' },
    { id: 2814, name: '甘南藏族自治州' }
  ],
  29: [ // 青海
    { id: 2901, name: '西宁市' },
    { id: 2902, name: '海东市' },
    { id: 2903, name: '海北藏族自治州' },
    { id: 2904, name: '黄南藏族自治州' },
    { id: 2905, name: '海南藏族自治州' },
    { id: 2906, name: '果洛藏族自治州' },
    { id: 2907, name: '玉树藏族自治州' },
    { id: 2908, name: '海西蒙古族藏族自治州' }
  ],
  30: [ // 宁夏
    { id: 3001, name: '银川市' },
    { id: 3002, name: '石嘴山市' },
    { id: 3003, name: '吴忠市' },
    { id: 3004, name: '固原市' },
    { id: 3005, name: '中卫市' }
  ],
  31: [ // 新疆
    { id: 3101, name: '乌鲁木齐市' },
    { id: 3102, name: '克拉玛依市' },
    { id: 3103, name: '吐鲁番市' },
    { id: 3104, name: '哈密市' },
    { id: 3105, name: '昌吉回族自治州' },
    { id: 3106, name: '博尔塔拉蒙古自治州' },
    { id: 3107, name: '巴音郭楞蒙古自治州' },
    { id: 3108, name: '阿克苏地区' },
    { id: 3109, name: '克孜勒苏柯尔克孜自治州' },
    { id: 3110, name: '喀什地区' },
    { id: 3111, name: '和田地区' },
    { id: 3112, name: '伊犁哈萨克自治州' },
    { id: 3113, name: '塔城地区' },
    { id: 3114, name: '阿勒泰地区' }
  ],
  32: [ // 台湾
    { id: 3201, name: '台北市' },
    { id: 3202, name: '高雄市' },
    { id: 3203, name: '台中市' },
    { id: 3204, name: '台南市' },
    { id: 3205, name: '基隆市' },
    { id: 3206, name: '新竹市' },
    { id: 3207, name: '嘉义市' }
  ],
  33: [ // 香港
    { id: 3301, name: '香港特别行政区' }
  ],
  34: [ // 澳门
    { id: 3401, name: '澳门特别行政区' }
  ]
};

// 初始化省份列表（从后端加载）
const loadProvinces = async () => {
  try {
    const response = await axios.post('gridProvince/listGridProvinceAll');
    if (response.data && response.data.length > 0) {
      provinceArr.value = response.data;
    } else {
      // 回退到本地数据，统一字段名为 provinceId/provinceName
      provinceArr.value = provincesData.map(p => ({
        provinceId: p.provinceId || p.id,
        provinceName: p.provinceName || p.name
      }));
    }
  } catch (e) {
    console.error('加载省份失败，使用默认数据:', e);
    provinceArr.value = provincesData.map(p => ({
      provinceId: p.provinceId || p.id,
      provinceName: p.provinceName || p.name
    }));
  }
};

// 根据选择的省份加载对应城市（从后端加载）
const handleProvinceChange = async (val) => {
  if (!val) {
    cityArr.value = [];
    cityId.value = null;
    return;
  }

  try {
    const response = await axios.post('gridCity/listGridCityByProvinceId', {
      provinceId: val
    });
    if (response.data && response.data.length > 0) {
      cityArr.value = response.data;
    } else {
      // 后端无数据时回退到本地城市列表，字段名统一为 cityId/cityName
      cityArr.value = (citiesMap[val] || []).map(c => ({
        cityId: c.cityId || c.id,
        cityName: c.cityName || c.name,
        provinceId: c.provinceId || val
      }));
    }
  } catch (e) {
    console.error('加载城市失败:', e);
    cityArr.value = (citiesMap[val] || []).map(c => ({
      cityId: c.cityId || c.id,
      cityName: c.cityName || c.name,
      provinceId: c.provinceId || val
    }));
  }
  cityId.value = null;
};

onMounted(() => {
  loadProvinces();
});

const toFeedbackList = () => {
  router.push('/feedbackList');
};

const goBack = () => {
  router.back();
};

const handleSubmit = () => {
  if (!provinceId.value) {
    ElMessage.warning('请选择省份');
    return;
  }
  if (!cityId.value) {
    ElMessage.warning('请选择城市');
    return;
  }
  if (!address.value || !address.value.trim()) {
    ElMessage.warning('请输入详细地址');
    return;
  }
  
  // 获取选中的省份和城市名称
  const selectedProvince = provinceArr.value.find(p => p.provinceId === provinceId.value);
  const selectedCity = cityArr.value.find(c => c.cityId === cityId.value);

  router.push({
    path: '/selectAqi',
    query: {
      provinceId: provinceId.value,
      provinceName: selectedProvince?.provinceName || selectedProvince?.name || '',
      cityId: cityId.value,
      cityName: selectedCity?.cityName || selectedCity?.name || '',
      address: address.value.trim()
    }
  });
};
</script>

<style scoped>
.wrapper {
  width: 100%;
  min-height: 100vh;
  background-color: #cfe8e9;
  padding-bottom: 40px;
}

header { 
  width: 100%; 
  max-width: 750px;
  margin: 0 auto;
  text-align: center;
}

header img { 
  display: block; 
  width: 100%;
  height: auto;
  max-width: 600px;
  margin: 0 auto;
}

h1 {
  text-align: center;
  color: #46a34f;
  font-size: 32px;
  font-weight: 700;
  margin: 20px 0 5px;
}

.subtitle {
  text-align: center;
  color: #d9534f;
  font-size: 20px;
  font-weight: bold;
  margin: 0 0 30px;
}

section {
  width: 86%;
  max-width: 500px;
  margin: 0 auto;
  background-color: #fff;
  border-radius: 12px;
  box-sizing: border-box;
  padding: 30px 30px 40px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.form-item {
  margin-bottom: 20px;
}

.form-item label {
  display: block;
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
  font-weight: 500;
}

.button-box {
  width: 100%;
  height: 50px;
  border-radius: 8px;
  background-color: #70b0bc;
  color: #fff;
  text-align: center;
  line-height: 50px;
  font-size: 18px;
  letter-spacing: 2px;
  user-select: none;
  margin-top: 20px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.button-box:hover {
  background-color: #5a9aa6;
}

.secondary-buttons {
  margin-top: 15px;
}

.btn-secondary {
  width: 100%;
  height: 45px;
  border-radius: 8px;
  background-color: #fff;
  color: #70b0bc;
  border: 2px solid #70b0bc;
  text-align: center;
  line-height: 41px;
  font-size: 16px;
  user-select: none;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-secondary:hover {
  background-color: #70b0bc;
  color: #fff;
}

/* Element Plus 组件样式覆盖 */
:deep(.el-select) {
  width: 100% !important;
}

:deep(.el-input__inner) {
  text-align: left !important;
}

:deep(.el-input__placeholder) {
  text-align: left !important;
}
</style>
