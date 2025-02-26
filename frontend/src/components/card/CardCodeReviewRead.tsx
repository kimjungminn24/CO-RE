import { ReviewType } from "../../Types/pullRequestType";
import {useUserStore} from "../../store/userStore";
import { CardCodeReviewReadLayout, CardCodeReviewReadBox, ContentBox, NameBox, NameSpan, HourSpan, BodyBox } from "./CardCodeReviewRead.styled";

interface CardCodeReviewReadProps{
    key:number;
    review: ReviewType
}

function CardCodeReviewRead({review}:CardCodeReviewReadProps) {
  const userInfo = useUserStore((state)=>state.userInfo)
  return (
    <CardCodeReviewReadLayout>
      <CardCodeReviewReadBox>
        {userInfo?.userInfo.image}
        <ContentBox>
          <NameBox>
            <NameSpan>{userInfo?.userInfo.id}</NameSpan>
            <HourSpan>Hour</HourSpan>
          </NameBox>
          <BodyBox>
          {review.body}
          </BodyBox>
        </ContentBox>
      </CardCodeReviewReadBox>
    </CardCodeReviewReadLayout>
  )
}

export default CardCodeReviewRead