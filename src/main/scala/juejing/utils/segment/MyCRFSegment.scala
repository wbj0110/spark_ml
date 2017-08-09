package utils.segment

import com.hankcs.hanlp.seg.CRF.CRFSegment

/**
  * 自定义CRF分词类，继承CRFSegment，并实现序列化
  *
  */
class MyCRFSegment extends CRFSegment with Serializable{

}
